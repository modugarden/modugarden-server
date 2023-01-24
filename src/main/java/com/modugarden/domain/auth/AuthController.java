package com.modugarden.domain.auth;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.dto.TokenDto;
import com.modugarden.domain.user.dto.request.LoginRequestDto;
import com.modugarden.domain.user.service.UserService2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
