package com.modugarden.domain.user.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.s3.FileService;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.dto.request.UserNicknameRequestDto;
import com.modugarden.domain.user.dto.response.UserInfoResponseDto;
import com.modugarden.domain.user.dto.response.UserNicknameFindResponseDto;
import com.modugarden.domain.user.dto.response.UserNicknameResponseDto;
import com.modugarden.domain.user.dto.response.UserProfileImgResponseDto;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FileService fileService;

    public Slice<UserNicknameFindResponseDto> findByNickname(Long userId, String nickname, Pageable pageable) {
        Slice<User> findUsers = userRepository.findByNicknameLike('%' + nickname + '%', pageable);
        Slice<UserNicknameFindResponseDto> result = findUsers
                .map(u -> new UserNicknameFindResponseDto(u.getId(), u.getNickname(), u.getProfileImg()
                        , userRepository.readUserInterestCategory((u.getId()))
                        , followRepository.exists(userId, u.getId())));
        return result;
    }

    public UserInfoResponseDto readUserInfo(Long userId) {
        User user = userRepository.readUserInfo(userId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        List<String> categories = userRepository.readUserInterestCategory(userId);
        if (categories.isEmpty()) throw new BusinessException(ErrorMessage.CATEGORY_NOT_FOUND);
        return new UserInfoResponseDto(user.getEmail(), user.getNickname(), user.getBirth(), user.getAuthority(), user.getProfileImg(), categories);
    }

    @Transactional
    public UserNicknameResponseDto updateUserNickname(Long userId, UserNicknameRequestDto userNicknameRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        user.updateNickname(userNicknameRequestDto.getNickname());
        return new UserNicknameResponseDto(user.getNickname());
    }

    @Transactional
    public UserProfileImgResponseDto updateProfileImg(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        String profileImageUrl = fileService.uploadFile(file, userId, "profileImage");
        user.updateProfileImage(profileImageUrl);
        return new UserProfileImgResponseDto(user.getProfileImg());
    }
}
