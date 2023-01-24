package com.modugarden.domain.user.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.auth.dto.TokenDto;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;
import com.modugarden.domain.category.repository.UserInterestCategoryRepository;
import com.modugarden.domain.user.dto.request.LoginRequestDto;
import com.modugarden.domain.user.dto.request.SignUpRequestDto;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.entity.UserNotification;
import com.modugarden.domain.user.entity.enums.UserAuthority;
import com.modugarden.domain.user.repository.UserNotificationRepository;
import com.modugarden.domain.user.repository.UserRepository2;
import com.modugarden.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Boolean.TRUE;

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

    // 회원가입
    public Long SignupUser(SignUpRequestDto signUpRequestDto){
        // 이메일 중복 체크
        if(userRepository2.existsByEmail(signUpRequestDto.getEmail())){
            // 에러 처리 필요
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
                .nickname(signUpRequestDto.getNickname())
                .authority(UserAuthority.GENERAL)
                .notification(userNotification)
                .build();
        
        signUpUser.encodePassword(passwordEncoder); // 비밀번호 암호화
        
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

    public TokenDto login(LoginRequestDto loginRequestDto){
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

            return TokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        }catch (BadCredentialsException e){
            System.out.println(e.getMessage());
            throw new BusinessException(ErrorMessage.WRONG_PASSWORD);
        }
    }
}
