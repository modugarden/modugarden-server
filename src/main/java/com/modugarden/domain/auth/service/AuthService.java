package com.modugarden.domain.auth.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.error.exception.custom.InvalidTokenException;
import com.modugarden.common.error.exception.custom.LoginCancelException;
import com.modugarden.domain.auth.dto.request.IsEmailDuplicatedRequestDto;
import com.modugarden.domain.auth.dto.request.TokenReissueRequestDto;
import com.modugarden.domain.auth.dto.response.IsEmailDuplicatedResponseDto;
import com.modugarden.domain.auth.dto.response.LoginResponseDto;
import com.modugarden.domain.auth.entity.RefreshToken;
import com.modugarden.domain.auth.repository.RefreshTokenRepository;
import com.modugarden.domain.block.repository.BlockRepository;
import com.modugarden.domain.board.service.BoardService;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;
import com.modugarden.domain.category.repository.UserInterestCategoryRepository;
import com.modugarden.domain.comment.service.CommentService;
import com.modugarden.domain.curation.service.CurationService;
import com.modugarden.domain.fcm.repository.FcmRepository;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.report.repository.ReportUserRepository;
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
import com.modugarden.domain.user.repository.UserRepository;
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
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final InterestCategoryRepository interestCategoryRepository;
    private final UserInterestCategoryRepository UICRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final BoardService boardService;
    private final CurationService curationService;

    private final BlockRepository blockRepository;
    private final ReportUserRepository reportUserRepository;
    private final FcmRepository fcmRepository;
    private final FollowRepository followRepository;
    private final CommentService commentService;
    /**
     * 회원 가입
     * 소셜 로그인 유저도 해당 회원가입 함수를 사용
     */
    @Transactional
    public Long signupUser(SignUpRequestDto signUpRequestDto){

        // 이메일 중복 체크
        if(userRepository.existsByEmail(signUpRequestDto.getEmail())){
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

        userRepository.save(signUpUser);

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

    /**
     * 일반 로그인
     */
    @Transactional
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
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

            // 로그인 response 생성
            return LoginResponseDto.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessToken_expiredDate(expiration)
                    .build();

        }catch (BadCredentialsException e){
            System.out.println(e.getMessage());
            throw new LoginCancelException(ErrorMessage.WRONG_PASSWORD);
        }
    }

    /**
     * 소셜 로그인
     */
    @Transactional
    public LoginResponseDto socialLogin(SocialLoginRequestDto requestDto){
        // 이미 소셜로그인은 성공한 상태로 호출됨
        User socialLoginUser = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

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
            User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

            // 토큰 DTO생성
            return LoginResponseDto.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessToken_expiredDate(expiration)
                    .build();

        }catch (BadCredentialsException e){
            System.out.println(e.getMessage());
            throw new LoginCancelException(ErrorMessage.WRONG_PASSWORD);
        }
    }

    /**
     * 이메일 중복 여부 검증
     */
    public IsEmailDuplicatedResponseDto isEmailDuplicate(IsEmailDuplicatedRequestDto requestDto) {
        Boolean isDuplicate = userRepository.existsByEmail(requestDto.getEmail());
        return new IsEmailDuplicatedResponseDto(isDuplicate);
    }

    /**
     * 닉네임 중복 여부 검증
     */
    public NicknameIsDuplicatedResponseDto isNicknameDuplicate(NicknameIsDuplicatedRequestDto requestDto) {
        String userNickname = requestDto.getNickname().toLowerCase(); // 소문자 변환
        Boolean isDuplicate = userRepository.existsByNickname(userNickname);
        return new NicknameIsDuplicatedResponseDto(isDuplicate, userNickname);
    }

    /**
     * accessToken 재 발급
     */
    @Transactional
    public LoginResponseDto reissueAccessToken(TokenReissueRequestDto requestDto) {
        // 1. access Token 유효성 검증
        tokenProvider.validateAccessTokenForReissue(requestDto.getAccessToken());

        // 2. access Token에서 user email 가져옴
        String userEmail = tokenProvider.parseClaims(requestDto.getAccessToken()).getSubject();

        // 3. access Token이 만료되지 않았다면, refresh Token이 탈취되었다고 판단하여 재로그인 필요 메세지 전달
        Date expiration = tokenProvider.parseClaims(requestDto.getAccessToken()).getExpiration();

        if(!expiration.before(new Date())){ // 만료여부 체크 하는 거 헷갈림
            throw new InvalidTokenException(WRONG_REISSUE_TOKEN_ACCESS);
        }

        // 4. refresh Token 유효성 검증
        tokenProvider.validateToken(requestDto.getRefreshToken());// validate 함수 내에서 에러 핸들링 throw

        // 5. Redis에서 user email을 기반으로 저장된 RefreshToken값을 가져옴
        RefreshToken redisRefreshToken = refreshTokenRepository.findById(userEmail).orElseThrow(() -> new BusinessException(WRONG_JWT_TOKEN));// accessToken속 정보와 일치하는 refreshToken이 존재하지 않음. 재 로그인 필요.
       // System.out.println("redisRefreshToken = " + redisRefreshToken.getRefreshToken());

        if(!redisRefreshToken.getRefreshToken().equals(requestDto.getRefreshToken())){ // request로 받은 refreshToken과 redis에 저장된 refreshToken 비교
            throw new InvalidTokenException(WRONG_JWT_TOKEN);// refresh Token 정보가 일치하지 않습니다.
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
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return LoginResponseDto.builder()
                .userId(user.getId())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessToken_expiredDate(newExpiration.toString())
                .build();
    }

    /**
     * 유저 탈퇴
     */
    @Transactional
    public DeleteUserResponseDto deleteCurrentUser(User user){
        // 큐레이션 삭제
        curationService.deleteAllCurationOfUser(user);

        // 포스트 삭제
        boardService.deleteAllBoardOfUser(user);

        // fcm 토큰 삭제
        fcmRepository.deleteByUser(user);

        // 차단 유저
        blockRepository.deleteByUser(user);

        // 유저 신고 삭제
        reportUserRepository.deleteByUser(user);

        // 팔로우 삭제
        followRepository.deleteByFollowingUser(user);
        followRepository.deleteByUser(user);

        // 댓글 삭제
        commentService.deleteAllCommentOfUser(user);

        // 회원 관심사 카테고리 삭제
        List<UserInterestCategory> userInterestCategories = UICRepository.findByUser(user);
        for (UserInterestCategory userInterestCategory : userInterestCategories) {
            UICRepository.deleteById(userInterestCategory.getId());
        }

        // 유저 삭제
        userRepository.deleteById(user.getId());

        // 알림 삭제
        userNotificationRepository.delete(user.getNotification());

        return new DeleteUserResponseDto(user.getId());
    }
}
