package com.modugarden.domain.user.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.user.dto.SignUpRequestDto;
import com.modugarden.domain.user.dto.SignUpResponseDto;
import com.modugarden.domain.user.service.UserService2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController2 {

    private final UserService2 userService2;

    @PostMapping("/sign-up")
    public BaseResponseDto<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        Long userId = userService2.SignupUser(signUpRequestDto);
        return new BaseResponseDto(new SignUpResponseDto(userId));
    }
}
