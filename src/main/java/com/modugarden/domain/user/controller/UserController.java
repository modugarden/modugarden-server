package com.modugarden.domain.user.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.user.dto.request.UpdateNotificationRequestDto;
import com.modugarden.domain.user.dto.request.UpdateUserCategoryRequestDto;
import com.modugarden.domain.user.dto.request.UserNicknameRequestDto;
import com.modugarden.domain.user.dto.response.*;
import com.modugarden.domain.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "탐색 피드 - 회원 닉네임 검색", notes = "해당 문자열을 포함한 닉네임을 가진 회원의 정보를 조회한다.")
    @GetMapping("")
    public SliceResponseDto<UserNicknameFindResponseDto> findByNickname(@AuthenticationPrincipal ModugardenUser user, @RequestParam(value = "nickname") @Valid String nickname, Pageable pageable) {
        return new SliceResponseDto<UserNicknameFindResponseDto>(userService.findByNickname(user.getUserId(), nickname, pageable));
    }

    @ApiOperation(value = "남의 프로필 - 메인 정보 조회", notes = "회원의 id로 회원의 프로필 메인 정보를 조회한다.")
    @GetMapping("/{userId}/info")
    public BaseResponseDto<UserInfoResponseDto> readUserInfo(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long userId) {
        return new BaseResponseDto<>(userService.readUserInfo(user.getUserId(), userId));
    }

    @ApiOperation(value = "내 프로필 - 메인 정보 조회", notes = "내 프로필 메인 정보를 조회한다.")
    @GetMapping("/me/info")
    public BaseResponseDto<CurrentUserInfoResponseDto> currentUserInfo(@AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.readCurrentUserInfo(user.getUserId()));
    }

    @ApiOperation(value = "내 프로필 - 프로필 설정", notes = "내 프로필에서 프로필 설정시 나타나는 정보들을 조회한다.")
    @GetMapping("/me/setting-info")
    public BaseResponseDto<UserSettingInfoResponseDto> readUserInfo(@AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.readUserSettingInfo(user.getUserId()));
    }

    @ApiOperation(value = "내 프로필 - 프로필 설정 - 사진 변경", notes = "내 프로필 - 프로필 설정에서 사진을 변경한다.")
    @PatchMapping(value = "/me/profileImg", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<UserProfileImgResponseDto> updateProfileImg(@RequestPart(name = "file", required = false) MultipartFile file, @AuthenticationPrincipal ModugardenUser user) throws IOException {
        return new BaseResponseDto<>(userService.updateProfileImg(user.getUserId(), file));
    }

    @ApiOperation(value = "내 프로필 - 프로필 설정 - 닉네임 변경", notes = "내 프로필 - 프로필 설정에서 닉네임을 변경한다.")
    @PatchMapping("/me/nickname")
    public BaseResponseDto<UserNicknameResponseDto> updateUserNickname(@RequestBody @Valid UserNicknameRequestDto userNicknameRequestDto, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.updateUserNickname(user.getUserId(), userNicknameRequestDto));
    }

    @ApiOperation(value = "내 프로필 - 프로필 설정 - 카테고리 변경", notes = "내 프로필 - 프로필 설정에서 카테고리를 변경한다.")
    @PatchMapping("/me/category")
    public BaseResponseDto<UpdateUserCategoryResponseDto> updateUserCategory(@RequestBody @Valid UpdateUserCategoryRequestDto updateUserCategoryRequestDto
                                                                            , @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.updateUserCategory(user.getUser(), updateUserCategoryRequestDto));
    }

    @ApiOperation(value = "내 프로필 - 알림 설정", notes = "내 프로필 - 알림 설정에서 알림을 설정한다.")
    @PatchMapping("/me/notification")
    public BaseResponseDto<UpdateNotificationResponseDto> updateNotification(@RequestBody @Valid UpdateNotificationRequestDto updateNotificationRequestDto
                                                                            , @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.updateNotification(user.getUser(), updateNotificationRequestDto));
    }

    @ApiOperation(value = "내 프로필 - 알림 설정 - 현재 알림 설정 조회", notes = "내 프로필 - 알림 설정 - 현재 알림 설정을 조회한다.")
    @GetMapping("/me/notification")
    public BaseResponseDto<UserNotificationResponseDto> readUserNotification(@AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.readUserNotification(user.getUser()));
    }
}