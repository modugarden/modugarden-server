package com.modugarden.domain.user.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.user.dto.UserInfoResponseDto;
import com.modugarden.domain.user.dto.UserNicknameFindResponseDto;
import com.modugarden.domain.user.dto.UserNicknameResponseDto;
import com.modugarden.domain.user.dto.request.UserNicknameRequestDto;
import com.modugarden.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public SliceResponseDto<UserNicknameFindResponseDto> findByNickname(@RequestParam @Valid String nickname, Pageable pageable) {
        return new SliceResponseDto<>(userService.findByNickname(nickname, pageable));
    }

    @GetMapping("/{userId}/info")
    public BaseResponseDto<UserInfoResponseDto> readUserInfo(@PathVariable Long userId) {
        return new BaseResponseDto<>(userService.readUserInfo(userId));
    }

    @PatchMapping("/{userId}/nickname") //me로 바뀌어야함
    public BaseResponseDto<UserNicknameResponseDto> updateUserNickname(@RequestBody @Valid UserNicknameRequestDto userNicknameRequestDto,
                                                                       @PathVariable Long userId) {
        return new BaseResponseDto<>(userService.updateUserNickname(userId, userNicknameRequestDto));
    }
//    @GetMapping("/me/info")
//    public BaseResponseDto<UserInfoResponseDto> currentUserInfo(@AuthenticationPrincipal User user) {
//        return new BaseResponseDto<>(userService.currentUserInfo(user.getId()));
//    }

//    @GetMapping("/blocked-list/{userId}")
//    public SliceResponseDto<UserBlockResponseDto> readBlockUser(@PathVariable Long userId, Pageable pageable) {
//        return new SliceResponseDto<>(userService.readBlockUser(userId, pageable));
//    }
}