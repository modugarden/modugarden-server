package com.modugarden.domain.user.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.user.dto.request.UserNicknameRequestDto;
import com.modugarden.domain.user.dto.request.UserProfileImgRequestDto;
import com.modugarden.domain.user.dto.response.UserInfoResponseDto;
import com.modugarden.domain.user.dto.response.UserNicknameFindResponseDto;
import com.modugarden.domain.user.dto.response.UserNicknameResponseDto;
import com.modugarden.domain.user.dto.response.UserProfileImgResponseDto;
import com.modugarden.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public SliceResponseDto<UserNicknameFindResponseDto> findByNickname(@AuthenticationPrincipal ModugardenUser user, @RequestParam(value = "nickname") @Valid String nickname, Pageable pageable) {
        return new SliceResponseDto<UserNicknameFindResponseDto>(userService.findByNickname(user.getUserId(), nickname, pageable));
    }

    @GetMapping("/{userId}/info")
    public BaseResponseDto<UserInfoResponseDto> readUserInfo(@PathVariable Long userId) {
        return new BaseResponseDto<>(userService.readUserInfo(userId));
    }

    @PatchMapping("/me/nickname")
    public BaseResponseDto<UserNicknameResponseDto> updateUserNickname(@RequestBody @Valid UserNicknameRequestDto userNicknameRequestDto, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.updateUserNickname(user.getUserId(), userNicknameRequestDto));
    }

    @PatchMapping(value = "/me/profileImg", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<UserProfileImgResponseDto> updateProfileImg(@RequestPart(name = "file", required = false) MultipartFile file, @AuthenticationPrincipal ModugardenUser user) throws IOException {
        return new BaseResponseDto<>(userService.updateProfileImg(user.getUserId(), file));
    }

    @GetMapping("/me/info")
    public BaseResponseDto<UserInfoResponseDto> currentUserInfo(@AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(userService.readUserInfo(user.getUserId()));
    }

//    @GetMapping("/blocked-list/{userId}")
//    public SliceResponseDto<UserBlockResponseDto> readBlockUser(@PathVariable Long userId, Pageable pageable) {
//        return new SliceResponseDto<>(userService.readBlockUser(userId, pageable));
//    }
}