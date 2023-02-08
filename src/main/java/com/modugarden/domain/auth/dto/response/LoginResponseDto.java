package com.modugarden.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private Long userId;
    private String nickname;

    private String accessToken;
    private String refreshToken;
    //private Date aceessToken_expiredDate;
    private String accessToken_expiredDate;

    @Builder
    public LoginResponseDto(Long userId, String nickname, String accessToken, String refreshToken, String accessToken_expiredDate) {
        this.userId = userId;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessToken_expiredDate = accessToken_expiredDate;
    }
}
