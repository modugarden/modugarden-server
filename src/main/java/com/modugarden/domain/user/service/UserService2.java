package com.modugarden.domain.user.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.auth.dto.IsEmailDuplicatedRequestDto;
import com.modugarden.domain.auth.dto.IsEmailDuplicatedResponseDto;
import com.modugarden.domain.auth.dto.LoginResponseDto;
import com.modugarden.domain.auth.dto.TokenReissueRequestDto;
import com.modugarden.domain.auth.entity.RefreshToken;
import com.modugarden.domain.auth.refreshToken.repository.RefreshTokenRepository;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;
import com.modugarden.domain.category.repository.UserInterestCategoryRepository;
import com.modugarden.domain.user.dto.request.LoginRequestDto;
import com.modugarden.domain.user.dto.request.NicknameIsDuplicatedRequestDto;
import com.modugarden.domain.user.dto.request.SignUpRequestDto;
import com.modugarden.domain.user.dto.request.SocialLoginRequestDto;
import com.modugarden.domain.user.dto.response.DeleteUserResponseDto;
import com.modugarden.domain.user.dto.response.NicknameIsDuplicatedResponseDto;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.entity.UserNotification;
import com.modugarden.domain.user.entity.enums.UserAuthority;
import com.modugarden.domain.user.repository.UserNotificationRepository;
import com.modugarden.domain.user.repository.UserRepository2;
import com.modugarden.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.modugarden.common.error.enums.ErrorMessage.*;
import static java.lang.Boolean.TRUE;
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService2 {

    private final UserRepository2 userRepository2;
    private final InterestCategoryRepository interestCategoryRepository;
    private final UserInterestCategoryRepository UICRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    // 소셜 로그인 유저도 해당 회원가입 함수를 사용, 비번은 프론트에서 랜덤비번을 생성해서 줌.
    public Long signupUser(SignUpRequestDto signUpRequestDto){

        // 이메일 중복 체크
        if(userRepository2.existsByEmail(signUpRequestDto.getEmail())){
            throw new BusinessException(ALREADY_SIGNUPED_EMAIL_USER);
        }

        // 유저 알람 정보 생성, 저장
        UserNotification userNotification = UserNotification.builder()
                .serviceOnOff(TRUE)
                .followOnOff(TRUE)
                .marketingOnOff(TRUE)
                .commentOnOff(TRUE)
                .build();

        userNotificationRepository.save(userNotification);

        // 유저 생성, 저장
        User signUpUser = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .birth(signUpRequestDto.getBirth())
                .nickname(signUpRequestDto.getNickname().toLowerCase())// 대문자 들어와도 소문자로 저장
                .authority(UserAuthority.ROLE_GENERAL)
                .notification(userNotification)
                .build();

        // 소셜로그인인 경우에는 비밀번호 암호화하지 않음
        if(!signUpRequestDto.getIsSocialLogin()){
            System.out.println("소셜로그인 아님");
            signUpUser.encodePassword(passwordEncoder); // 비밀번호 암호화
        }

        userRepository2.save(signUpUser);

        // 카테고리-유저 생성, 저장
        for (String category: signUpRequestDto.getCategories()) {
            InterestCategory interestCategory = interestCategoryRepository.findByCategory(category).get();
            UserInterestCategory userInterestCategory = UserInterestCategory.builder()
                    .user(signUpUser)
                    .category(interestCategory)
                    .build();

            UICRepository.save(userInterestCategory);
        }

        // 유저id 반환
        return signUpUser.getId();
    }


    public LoginResponseDto generalLogin(LoginRequestDto loginRequestDto){
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);
            String expiration = tokenProvider.parseClaims(accessToken).getExpiration().toString();

            System.out.println("일반 로그인 expiration = " + expiration);

            // 4. RefreshToken Redis에 저장
            String userEmail = authentication.getName();
            refreshTokenRepository.save(new RefreshToken(userEmail, refreshToken));

            // 유저 이메일로 유저 가져오기
            User user = userRepository2.findByEmail(userEmail).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

            // 로그인 response 생성
            return LoginResponseDto.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .aceessToken_expiredDate(expiration)
                    .build();

        }catch (BadCredentialsException e){
            System.out.println(e.getMessage());
            throw new BusinessException(ErrorMessage.WRONG_PASSWORD);
        }
    }

    public IsEmailDuplicatedResponseDto isEmailDuplicate(IsEmailDuplicatedRequestDto requestDto) {
        Boolean isDuplicate = userRepository2.existsByEmail(requestDto.getEmail());
        return new IsEmailDuplicatedResponseDto(isDuplicate);
    }

    public DeleteUserResponseDto deleteCurrentUser(User user){
        // User pk로 외래키 연관된 UserInterestCategory부터 삭제
        List<UserInterestCategory> userInterestCategories = UICRepository.findByUser(user);

          for (UserInterestCategory userInterestCategory : userInterestCategories) {
            UICRepository.deleteById(userInterestCategory.getId());
        }

        // 유저 삭제
        userRepository2.deleteById(user.getId());
        return new DeleteUserResponseDto(user.getId());
    }

    public NicknameIsDuplicatedResponseDto isNicknameDuplicate(NicknameIsDuplicatedRequestDto requestDto) {
        String userNickname = requestDto.getNickname().toLowerCase(); // 소문자 변환
        Boolean isDuplicate = userRepository2.existsByNickname(userNickname);
        return new NicknameIsDuplicatedResponseDto(isDuplicate, userNickname);
    }

    public LoginResponseDto socialLogin(SocialLoginRequestDto requestDto){
        // 이미 소셜로그인은 성공한 상태로 호출됨
        User socialLoginUser = userRepository2.findByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        // 암호화하기 전의 비번
        String password = socialLoginUser.getPassword();

        // 비번 잠시 암호화했다가 나중에 다시 해제
        socialLoginUser.encodePassword(passwordEncoder);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), password); // 이미 인증되었기 때문에 DB에 저장된 password 넣어줌

        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);// 비번 검증

            socialLoginUser.setOriginalPasswordOfSocialLoginUser(password); // 다시 비번 회복

            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);
            String expiration = tokenProvider.parseClaims(accessToken).getExpiration().toString();

            // 4. RefreshToken Redis에 저장
            refreshTokenRepository.save(new RefreshToken(requestDto.getEmail(), refreshToken));

            // 유저 이메일로 유저 가져오기
            User user = userRepository2.findByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

            // 토큰 DTO생성
            return LoginResponseDto.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .aceessToken_expiredDate(expiration)
                    .build();

        }catch (BadCredentialsException e){
            System.out.println(e.getMessage());
            throw new BusinessException(ErrorMessage.WRONG_PASSWORD);
        }
    }


    public LoginResponseDto reissueAccessToken(TokenReissueRequestDto requestDto) throws BusinessException{
        // 1. access Token 유효성 검증
        tokenProvider.validateAccessTokenForReissue(requestDto.getAccessToken());

        // 2. access Token에서 user email 가져옴
        String userEmail = tokenProvider.parseClaims(requestDto.getAccessToken()).getSubject();

        // 3. access Token이 만료되지 않았다면, refresh Token이 탈취되었다고 판단하여 재로그인 필요 메세지 전달
        Date expiration = tokenProvider.parseClaims(requestDto.getAccessToken()).getExpiration();

        if(!expiration.before(new Date())){ // 만료여부 체크 하는 거 헷갈림
            throw new BusinessException(WRONG_REISSUE_TOKEN_ACCESS);
        }

        // 4. refresh Token 유효성 검증
        tokenProvider.validateToken(requestDto.getRefreshToken());// validate 함수 내에서 에러 핸들링 throw

        // 5. Redis에서 user email을 기반으로 저장된 RefreshToken값을 가져옴
        RefreshToken redisRefreshToken = refreshTokenRepository.findById(userEmail).orElseThrow(() -> new BusinessException(WRONG_JWT_TOKEN));// accessToken속 정보와 일치하는 refreshToken이 존재하지 않음. 재 로그인 필요.
       // System.out.println("redisRefreshToken = " + redisRefreshToken.getRefreshToken());

        if(!redisRefreshToken.getRefreshToken().equals(requestDto.getRefreshToken())){ // request로 받은 refreshToken과 redis에 저장된 refreshToken 비교
            throw new BusinessException(WRONG_JWT_TOKEN);// refresh Token 정보가 일치하지 않습니다.
        }

        // 6. 새로운 토큰 생성
        Authentication authentication = tokenProvider.getAuthentication(requestDto.getAccessToken());
        String newAccessToken = tokenProvider.createAccessToken(authentication);
        String newRefreshToken = tokenProvider.createRefreshToken(authentication);
        Date newExpiration = tokenProvider.parseClaims(newAccessToken).getExpiration();


        // 7. RefreshToken Redis 업데이트(accessToken reissue시 refreshToken도 함께 재발급)
        refreshTokenRepository.delete(redisRefreshToken);
        refreshTokenRepository.save(new RefreshToken(userEmail, newRefreshToken));
        System.out.println("newRefreshToken = " + newRefreshToken);

        // 8. 유저 이메일로 유저 가져오기
        User user = userRepository2.findByEmail(userEmail).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return LoginResponseDto.builder()
                .userId(user.getId())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .aceessToken_expiredDate(newExpiration.toString())
                .build();
    }
}
