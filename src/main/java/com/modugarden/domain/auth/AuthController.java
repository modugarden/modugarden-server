package com.modugarden.domain.auth;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.dto.IsEmailDuplicatedRequestDto;
import com.modugarden.domain.auth.dto.IsEmailDuplicatedResponseDto;
import com.modugarden.domain.auth.dto.TokenDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.user.dto.request.LoginRequestDto;
import com.modugarden.domain.user.dto.response.DeleteUserResponseDto;
import com.modugarden.domain.user.service.UserService2;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class AuthController {
    private final UserService2 userService2;

    @PostMapping("/log-in")
    public BaseResponseDto<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto){
        TokenDto tokenDto = userService2.login(loginRequestDto);
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
}
