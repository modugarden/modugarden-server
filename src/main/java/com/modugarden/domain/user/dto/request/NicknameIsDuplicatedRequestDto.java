package com.modugarden.domain.user.dto.request;

import lombok.Getter;

@Getter
public class NicknameIsDuplicatedRequestDto {
    // validation 추가
    private String nickname;
}
