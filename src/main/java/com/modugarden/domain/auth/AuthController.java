package com.modugarden.domain.auth;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.dto.*;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.user.dto.request.LoginRequestDto;
import com.modugarden.domain.user.dto.request.SocialLoginRequestDto;
import com.modugarden.domain.user.dto.response.DeleteUserResponseDto;
import com.modugarden.domain.user.service.UserService2;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class AuthController {
    private final UserService2 userService2;
    private final EmailService emailService;

    @PostMapping("/log-in")
    public BaseResponseDto<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto tokenDto = userService2.generalLogin(loginRequestDto);
        return new BaseResponseDto<>(tokenDto);
    }

    @PostMapping("/sign-up/email/isDuplicated")
    public BaseResponseDto<IsEmailDuplicatedResponseDto> checkEmailDuplicated(@RequestBody IsEmailDuplicatedRequestDto requestDto){
        return new BaseResponseDto<>(userService2.isEmailDuplicate(requestDto));
    }

    @DeleteMapping("/me")
    public BaseResponseDto<DeleteUserResponseDto> deleteCurrentUser(@AuthenticationPrincipal ModugardenUser modugardenUser){
        return new BaseResponseDto<>(userService2.deleteCurrentUser(modugardenUser.getUser()));
    }

    @PostMapping("/sign-up/email/authentication")
    public BaseResponseDto<EmailAuthResponseDto> mailConfirm(@RequestBody EmailAuthRequestDto requestDto){
        return new BaseResponseDto<>(emailService.sendEmail(requestDto));
    }

    @PostMapping("/log-in/social")
    public BaseResponseDto<LoginResponseDto> googleLogin(@RequestBody SocialLoginRequestDto requestDto){
        LoginResponseDto tokenDto = userService2.socialLogin(requestDto);
        return new BaseResponseDto<>(tokenDto);
    }

    @PostMapping("/token-reissue")
    public BaseResponseDto<LoginResponseDto> reissueToken(@RequestBody @Valid TokenReissueRequestDto requestDto){
        LoginResponseDto tokenDto = userService2.reissueAccessToken(requestDto);
        return new BaseResponseDto<>(tokenDto);
    }

}
