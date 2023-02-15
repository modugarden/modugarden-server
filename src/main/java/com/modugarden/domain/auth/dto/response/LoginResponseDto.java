package com.modugarden.domain.auth.dto.response;

import com.modugarden.domain.user.entity.enums.UserAuthority;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private UserAuthority userAuthority;
    private String accessToken;
    private String refreshToken;
    private String accessToken_expiredDate;

    @Builder
    public LoginResponseDto(Long userId, String nickname, String profileImage, String accessToken, String refreshToken, String accessToken_expiredDate, UserAuthority userAuthority) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.userAuthority = userAuthority;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessToken_expiredDate = accessToken_expiredDate;
    }
}
