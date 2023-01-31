package com.modugarden.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private Long userId;

    private String accessToken;
    private String refreshToken;
    //private Date aceessToken_expiredDate;
    private String aceessToken_expiredDate;

    @Builder
    public LoginResponseDto(Long userId, String accessToken, String refreshToken, String aceessToken_expiredDate) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.aceessToken_expiredDate = aceessToken_expiredDate;
    }
}
