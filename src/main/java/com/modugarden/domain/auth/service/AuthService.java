package com.modugarden.domain.auth.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.error.exception.custom.InvalidTokenException;
import com.modugarden.common.error.exception.custom.LoginCancelException;
import com.modugarden.common.s3.FileService;
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
    private final FileService fileService;

    /**
     * ?????? ??????
     * ?????? ????????? ????????? ?????? ???????????? ????????? ??????
     */
    @Transactional
    public Long signupUser(SignUpRequestDto signUpRequestDto){

        // ????????? ?????? ??????
        if(userRepository.existsByEmail(signUpRequestDto.getEmail())){
            throw new BusinessException(ALREADY_SIGNUPED_EMAIL_USER);
        }

        // ?????? ?????? ?????? ??????, ??????
        UserNotification userNotification = UserNotification.builder()
                .serviceOnOff(TRUE)
                .followOnOff(TRUE)
                .marketingOnOff(TRUE)
                .commentOnOff(TRUE)
                .build();

        userNotificationRepository.save(userNotification);

        // ?????? ??????, ??????
        User signUpUser = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .birth(signUpRequestDto.getBirth())
                .nickname(signUpRequestDto.getNickname().toLowerCase())// ????????? ???????????? ???????????? ??????
                .authority(UserAuthority.ROLE_GENERAL)
                .notification(userNotification)
                .build();

        // ?????????????????? ???????????? ???????????? ??????????????? ??????
        if(!signUpRequestDto.getIsSocialLogin()){
            System.out.println("??????????????? ??????");
            signUpUser.encodePassword(passwordEncoder); // ???????????? ?????????
        }

        userRepository.save(signUpUser);

        // ????????????-?????? ??????, ??????
        for (String category: signUpRequestDto.getCategories()) {
            InterestCategory interestCategory = interestCategoryRepository.findByCategory(category).get();
            UserInterestCategory userInterestCategory = UserInterestCategory.builder()
                    .user(signUpUser)
                    .category(interestCategory)
                    .build();

            UICRepository.save(userInterestCategory);
        }

        // ??????id ??????
        return signUpUser.getId();
    }

    /**
     * ?????? ?????????
     */
    @Transactional
    public LoginResponseDto generalLogin(LoginRequestDto loginRequestDto){
        // 1. Login ID/PW ??? ???????????? Authentication ?????? ??????
        // ?????? authentication ??? ?????? ????????? ???????????? authenticated ?????? false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // 2. ?????? ?????? (????????? ???????????? ??????)??? ??????????????? ??????
        // authenticate ???????????? ????????? ??? CustomUserDetailsService ?????? ?????? loadUserByUsername ???????????? ??????
        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. ?????? ????????? ???????????? JWT ?????? ??????
            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);
            String expiration = tokenProvider.parseClaims(accessToken).getExpiration().toString();

            // 4. RefreshToken Redis??? ??????
            String userEmail = authentication.getName();
            refreshTokenRepository.save(new RefreshToken(userEmail, refreshToken));

            // ?????? ???????????? ?????? ????????????
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

            // ????????? response ??????
            return LoginResponseDto.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImg())
                    .userAuthority(user.getAuthority())
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
     * ?????? ?????????
     */
    @Transactional
    public LoginResponseDto socialLogin(SocialLoginRequestDto requestDto){
        // ?????? ?????????????????? ????????? ????????? ?????????
        User socialLoginUser = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        // ??????????????? ?????? ??????
        String password = socialLoginUser.getPassword();

        // ?????? ?????? ?????????????????? ????????? ?????? ?????? -> userDetails??? getPassword????????? ???????????? password??? ?????????
        socialLoginUser.encodePassword(passwordEncoder);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), password); // ?????? ??????????????? ????????? DB??? ????????? password ?????????

        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);// ?????? ??????

            socialLoginUser.setOriginalPasswordOfSocialLoginUser(password); // ?????? ?????? ??????

            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);
            String expiration = tokenProvider.parseClaims(accessToken).getExpiration().toString();

            // 4. RefreshToken Redis??? ??????
            refreshTokenRepository.save(new RefreshToken(requestDto.getEmail(), refreshToken));

            // ?????? ???????????? ?????? ????????????
            User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

            // ?????? DTO??????
            return LoginResponseDto.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImg())
                    .userAuthority(user.getAuthority())
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
     * ????????? ?????? ?????? ??????
     */
    public IsEmailDuplicatedResponseDto isEmailDuplicate(IsEmailDuplicatedRequestDto requestDto) {
        Boolean isDuplicate = userRepository.existsByEmail(requestDto.getEmail());
        return new IsEmailDuplicatedResponseDto(isDuplicate);
    }

    /**
     * ????????? ?????? ?????? ??????
     */
    public NicknameIsDuplicatedResponseDto isNicknameDuplicate(NicknameIsDuplicatedRequestDto requestDto) {
        String userNickname = requestDto.getNickname().toLowerCase(); // ????????? ??????
        Boolean isDuplicate = userRepository.existsByNickname(userNickname);
        return new NicknameIsDuplicatedResponseDto(isDuplicate, userNickname);
    }

    /**
     * accessToken ??? ??????
     */
    @Transactional
    public LoginResponseDto reissueAccessToken(TokenReissueRequestDto requestDto) {
        // 1. access Token ????????? ??????
        tokenProvider.validateAccessTokenForReissue(requestDto.getAccessToken());

        // 2. access Token?????? user email ?????????
        String userEmail = tokenProvider.parseClaims(requestDto.getAccessToken()).getSubject();

        // 3. access Token??? ???????????? ????????????, refresh Token??? ?????????????????? ???????????? ???????????? ?????? ????????? ??????
        Date expiration = tokenProvider.parseClaims(requestDto.getAccessToken()).getExpiration();

        if(!expiration.before(new Date())){
            throw new InvalidTokenException(WRONG_REISSUE_TOKEN_ACCESS);
        }

        // 4. refresh Token ????????? ??????
        tokenProvider.validateToken(requestDto.getRefreshToken());// validate ?????? ????????? ?????? ????????? throw

        // 5. Redis?????? user email??? ???????????? ????????? RefreshToken?????? ?????????
        RefreshToken redisRefreshToken = refreshTokenRepository.findById(userEmail).orElseThrow(() -> new BusinessException(WRONG_JWT_TOKEN));// accessToken??? ????????? ???????????? refreshToken??? ???????????? ??????. ??? ????????? ??????.

        if(!redisRefreshToken.getRefreshToken().equals(requestDto.getRefreshToken())){ // request??? ?????? refreshToken??? redis??? ????????? refreshToken ??????
            throw new InvalidTokenException(WRONG_JWT_TOKEN);// refresh Token ????????? ???????????? ????????????.
        }

        // 6. ????????? ?????? ??????
        Authentication authentication = tokenProvider.getAuthentication(requestDto.getAccessToken());
        String newAccessToken = tokenProvider.createAccessToken(authentication);
        String newRefreshToken = tokenProvider.createRefreshToken(authentication);
        Date newExpiration = tokenProvider.parseClaims(newAccessToken).getExpiration();


        // 7. RefreshToken Redis ????????????(accessToken reissue??? refreshToken??? ?????? ?????????)
        refreshTokenRepository.delete(redisRefreshToken);
        refreshTokenRepository.save(new RefreshToken(userEmail, newRefreshToken));

        // 8. ?????? ???????????? ?????? ????????????
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return LoginResponseDto.builder()
                .userId(user.getId())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessToken_expiredDate(newExpiration.toString())
                .build();
    }

    /**
     * ?????? ??????
     */
    @Transactional
    public DeleteUserResponseDto deleteCurrentUser(User user){
        // ???????????? ??????
        curationService.deleteAllCurationOfUser(user);

        // ????????? ??????
        boardService.deleteAllBoardOfUser(user);

        // fcm ?????? ??????
        fcmRepository.deleteByUser(user);
        fcmRepository.flush();

        // ?????? ??????
        blockRepository.deleteByUser(user);
        blockRepository.flush();

        // ?????? ?????? ??????
        reportUserRepository.deleteByUser(user);
        reportUserRepository.flush();

        // ????????? ??????
        followRepository.deleteByFollowingUser(user);
        followRepository.deleteByUser(user);
        followRepository.flush();

        // ?????? ??????
        commentService.deleteAllCommentOfUser(user);

        // ?????? ????????? ???????????? ??????
        List<UserInterestCategory> userInterestCategories = UICRepository.findByUser(user);
        for (UserInterestCategory userInterestCategory : userInterestCategories) {
            UICRepository.deleteById(userInterestCategory.getId());
        }

        // ?????? ??????
        userRepository.deleteById(user.getId());

        // ?????? ?????? S3?????? ??????
        if(user.getProfileImg() != null){
            fileService.deleteFile(user.getProfileImg());
        }

        // ?????? ??????
        userNotificationRepository.delete(user.getNotification());

        return new DeleteUserResponseDto(user.getId());
    }
}
