package com.modugarden.domain.user.controller;

import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.user.dto.UserNicknameFindResponseDto;
import com.modugarden.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public SliceResponseDto<UserNicknameFindResponseDto> findByNickname(@RequestParam String nickname, Pageable pageable) {
        return new SliceResponseDto<>(userService.findByNickname(nickname, pageable));
    }
}