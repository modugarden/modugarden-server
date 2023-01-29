package com.modugarden.utils.jwt;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {
    @Value("${spring.security.jwt.header}")
    private String AUTHORIZATION_HEADER;

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    private long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(1).toMillis(); // 만료시간 30분

    private long REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(14).toMillis(); // 만료시간 2주

    private final UserDetailsService userDetailsService;

    private Key getSigninKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 객체 초기화, secretKey 를 Base64로 인코딩합니다. - 이미 인코딩된 값이라서 주석 처리
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }

    protected String createToken(Authentication authentication, long tokenValid) {
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 현재시간
        Date now = new Date();

        // payload 정보 생성
        // ex) sub :
        Claims claims = Jwts.claims().setSubject(authentication.getName()); // JWT payload 에 저장되는 정보단위 - 현재 : 이메일
        claims.put("roles", authorities); // 정보는 key/value 쌍으로 저장됩니다.

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(new Date()) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + tokenValid)) // 토큰 만료 시간
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)  // 사용할 암호화 알고리즘
                // signature 에 들어갈 secret 값 세팅
                .compact();
    }

    /**
     *
     * @param authentication
     * @return 엑세스 토큰 생성
     */
    public String createAccessToken(Authentication authentication){
        return createToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     *
     * @param authentication
     * @return 리프레시 토큰 생성
     */
    public String createRefreshToken(Authentication authentication){
        return createToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     *
     * @param token
     * @return JWT 토큰에서 인증 정보 조회
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token)); // 이거 이메일로 해야할것 같은데
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    /**
     *
     * @param accessToken
     * @return 토큰 파싱 후 클레임 반환
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) { // 만료된 토큰이더라도 일단 파싱을 함
            return e.getClaims(); // 만료된 토큰이더라도 반환하는 이유는 재발행 때문
        }
    }


    /**
     *
     * @param token
     * @return 토큰에서 유저 이메일 획득
     */
    public String getUserEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     *
     * @param request
     * @return Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     *
     * @param token
     * @return 토큰의 유효성 + 만료일자 확인
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new BusinessException(ErrorMessage.WRONG_JWT_SIGNITURE);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new BusinessException(ErrorMessage.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new BusinessException(ErrorMessage.NOT_APPLY_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new BusinessException(ErrorMessage.WRONG_JWT_TOKEN);
        }
    }
}