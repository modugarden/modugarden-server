package com.modugarden.domain.auth.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
