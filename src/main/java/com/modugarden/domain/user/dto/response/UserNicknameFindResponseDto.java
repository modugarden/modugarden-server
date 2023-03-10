package com.modugarden.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserNicknameFindResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private List<String> categories;
    private boolean isFollow;
    private boolean block;
    private boolean isBlocked;
    private List<String> fcmTokens;
}
