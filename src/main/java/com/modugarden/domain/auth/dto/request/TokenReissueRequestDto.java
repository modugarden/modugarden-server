package com.modugarden.domain.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenReissueRequestDto {
    @NotBlank(message = "accessToken 을 입력해주세요.")
    private String accessToken;

    @NotBlank(message = "refreshToken 을 입력해주세요.")
    private String refreshToken;
}
