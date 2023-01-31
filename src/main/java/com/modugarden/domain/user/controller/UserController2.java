package com.modugarden.domain.user.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.user.dto.request.NicknameIsDuplicatedRequestDto;
import com.modugarden.domain.user.dto.request.SignUpRequestDto;
import com.modugarden.domain.user.dto.response.NicknameIsDuplicatedResponseDto;
import com.modugarden.domain.user.dto.response.SignUpResponseDto;
import com.modugarden.domain.user.service.UserService2;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "서비스 시작 전 - 회원가입", notes = "회원 정보를 가지고 회원가입을 한다.")
    @PostMapping("/sign-up")
    public BaseResponseDto<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        Long userId = userService2.signupUser(signUpRequestDto);
        return new BaseResponseDto(new SignUpResponseDto(userId));
    }

    @ApiOperation(value = "서비스 시작 전 - 닉네임 중복 여부 조회", notes = "해당 닉네임을 가진 유저가 있는지 확인한다.")
    @PostMapping("/nickname/isDuplicated")
    public BaseResponseDto<NicknameIsDuplicatedResponseDto> isNicknameDuplicated(@RequestBody NicknameIsDuplicatedRequestDto requestDto){
        return new BaseResponseDto<>(userService2.isNicknameDuplicate(requestDto));
    }

}
