package com.modugarden.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

// 이미 로그인 성공은 한 뒤에 accessToken, refreshToken을 발급위함.
@Getter
public class SocialLoginRequestDto {
    @Email(message="이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;
}
