package com.modugarden.domain.user.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.user.dto.UserInfoResponseDto;
import com.modugarden.domain.user.dto.UserNicknameFindResponseDto;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public Slice<UserNicknameFindResponseDto> findByNickname(String nickname, Pageable pageable) {
        Slice<User> findUsers = userRepository.findByNicknameLike('%' + nickname + '%', pageable);
        Slice<UserNicknameFindResponseDto> result = findUsers.map(u -> new UserNicknameFindResponseDto(u.getId()));
        return result;
    }

    public UserInfoResponseDto readUserInfo(Long userId) {
        User user = userRepository.readUserInfo(userId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        List<String> categories = userRepository.readUserInterestCategory(userId);
        if (categories.isEmpty()) throw new BusinessException(ErrorMessage.CATEGORY_NOT_FOUND);
        return new UserInfoResponseDto(user.getEmail(), user.getNickname(), user.getBirth(), user.getAuthority(), user.getProfileImg(), categories);
    }

}
