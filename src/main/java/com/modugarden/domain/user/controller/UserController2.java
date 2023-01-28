package com.modugarden.domain.user.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.user.dto.request.SignUpRequestDto;
import com.modugarden.domain.user.dto.response.SignUpResponseDto;
import com.modugarden.domain.user.service.UserService2;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


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
