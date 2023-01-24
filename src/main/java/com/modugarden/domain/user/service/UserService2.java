package com.modugarden.domain.user.service;

import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;
import com.modugarden.domain.category.repository.UserInterestCategoryRepository;
import com.modugarden.domain.user.dto.SignUpRequestDto;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.entity.UserNotification;
import com.modugarden.domain.user.entity.enums.UserAuthority;
import com.modugarden.domain.user.repository.UserNotificationRepository;
import com.modugarden.domain.user.repository.UserRepository2;
import lombok.RequiredArgsConstructor;
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
}
