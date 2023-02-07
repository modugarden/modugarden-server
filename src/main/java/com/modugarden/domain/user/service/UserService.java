package com.modugarden.domain.user.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.s3.FileService;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;
import com.modugarden.domain.category.repository.UserInterestCategoryRepository;
import com.modugarden.domain.curation.repository.CurationRepository;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.dto.request.UpdateNotificationRequestDto;
import com.modugarden.domain.user.dto.request.UpdateUserCategoryRequestDto;
import com.modugarden.domain.user.dto.request.UpdateProfileRequestDto;
import com.modugarden.domain.user.dto.response.*;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.entity.UserNotification;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FileService fileService;
    private final BoardRepository boardRepository;
    private final CurationRepository curationRepository;
    private final UserInterestCategoryRepository userInterestCategoryRepository;
    private final InterestCategoryRepository interestCategoryRepository;


    public Slice<UserNicknameFindResponseDto> findByNickname(Long userId, String nickname, Pageable pageable) {
        Slice<User> findUsers = userRepository.findByNicknameLike('%' + nickname + '%', pageable);
        Slice<UserNicknameFindResponseDto> result = findUsers
                .map(u -> new UserNicknameFindResponseDto(u.getId(), u.getNickname(), u.getProfileImg()
                        , userRepository.readUserInterestCategory((u.getId()))
                        , followRepository.exists(userId, u.getId())));
        return result;
    }

    public CurrentUserInfoResponseDto readCurrentUserInfo(User user) {
        List<String> categories = userRepository.readUserInterestCategory(user.getId());
        if (categories.isEmpty()) throw new BusinessException(ErrorMessage.CATEGORY_NOT_FOUND);
        return new CurrentUserInfoResponseDto(user.getId(), user.getNickname(), user.getAuthority(), user.getProfileImg(),
                followRepository.countByFollowingUser_Id(user.getId()),
                boardRepository.countByUser_Id(user.getId()) + curationRepository.countByUser_Id(user.getId()), categories);
    }

    @Transactional
    public UpdateProfileResponseDto updateUserInfo(Long userId, MultipartFile file, UpdateProfileRequestDto updateProfileRequestDto) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        String userNickname = updateProfileRequestDto.getNickname().toLowerCase();
        String profileImageUrl = fileService.uploadFile(file, userId, "profileImage");
        user.updateUserInfo(userNickname, profileImageUrl);

        userInterestCategoryRepository.deleteAllByUser(user);
        List<String> categories = new ArrayList<>();
        for (String category: updateProfileRequestDto.getCategories()) {
            InterestCategory interestCategory = interestCategoryRepository.findByCategory(category).get();
            UserInterestCategory userInterestCategory = UserInterestCategory.builder()
                    .user(user)
                    .category(interestCategory)
                    .build();
            categories.add(category);
            userInterestCategoryRepository.save(userInterestCategory);
        }
        return new UpdateProfileResponseDto(userId, userNickname, user.getProfileImg(), categories);
    }

    public UserSettingInfoResponseDto readUserSettingInfo(User user) {
        List<String> categories = userRepository.readUserInterestCategory(user.getId());
        if (categories.isEmpty()) throw new BusinessException(ErrorMessage.CATEGORY_NOT_FOUND);
        return new UserSettingInfoResponseDto(user.getId(), user.getEmail(), user.getNickname(), user.getBirth(), user.getAuthority(), user.getProfileImg(), categories);
    }

    public UserInfoResponseDto readUserInfo(Long loginUserId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        List<String> categories = userRepository.readUserInterestCategory(userId);
        if (categories.isEmpty()) throw new BusinessException(ErrorMessage.CATEGORY_NOT_FOUND);
        return new UserInfoResponseDto(user.getId(), user.getNickname(), user.getAuthority(), user.getProfileImg(),
                followRepository.countByFollowingUser_Id(userId),
                boardRepository.countByUser_Id(userId) + curationRepository.countByUser_Id(userId),
                categories, followRepository.exists(loginUserId, userId));
    }

    @Transactional
    public UpdateNotificationResponseDto updateNotification(User user, UpdateNotificationRequestDto updateNotificationRequestDto) {
        User currentUser = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        currentUser.getNotification().updateNotification(updateNotificationRequestDto.getCommentOnOff(),
                updateNotificationRequestDto.getFollowOnOff(), updateNotificationRequestDto.getServiceOnOff(),
                updateNotificationRequestDto.getMarketingOnOff());
        return new UpdateNotificationResponseDto(currentUser.getId(), currentUser.getNotification().getCommentOnOff()
        , currentUser.getNotification().getFollowOnOff(), currentUser.getNotification().getServiceOnOff()
        , currentUser.getNotification().getMarketingOnOff());
    }

    public UserNotificationResponseDto readUserNotification(User user) {
        User currentUser = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        UserNotification userNotification = currentUser.getNotification();
        return new UserNotificationResponseDto(currentUser.getId(), userNotification.getCommentOnOff()
                , userNotification.getFollowOnOff(), userNotification.getServiceOnOff(), userNotification.getMarketingOnOff());
    }
}
