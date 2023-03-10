package com.modugarden.domain.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class EmailAuthRequestDto {
    @Email(message="이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;
}
