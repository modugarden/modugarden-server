package com.modugarden.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserInfoResponseDto {
    private String nickname;
    private String profileImage;
}
