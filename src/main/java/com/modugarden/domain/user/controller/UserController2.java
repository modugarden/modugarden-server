package com.modugarden.domain.user.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.user.dto.request.NicknameIsDuplicatedRequestDto;
import com.modugarden.domain.user.dto.request.SignUpRequestDto;
import com.modugarden.domain.user.dto.response.NicknameIsDuplicatedResponseDto;
import com.modugarden.domain.user.dto.response.SignUpResponseDto;
import com.modugarden.domain.user.service.UserService2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController2 {

    private final UserService2 userService2;

    @PostMapping("/sign-up")
    public BaseResponseDto<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        Long userId = userService2.signupUser(signUpRequestDto);
        return new BaseResponseDto(new SignUpResponseDto(userId));
    }

    @PostMapping("/nickname/isDuplicated")
    public BaseResponseDto<NicknameIsDuplicatedResponseDto> isNicknameDuplicated(@RequestBody NicknameIsDuplicatedRequestDto requestDto){
        return new BaseResponseDto<>(userService2.isNicknameDuplicate(requestDto));
    }

}
