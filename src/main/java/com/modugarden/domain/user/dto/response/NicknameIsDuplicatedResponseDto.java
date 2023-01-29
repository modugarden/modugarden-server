package com.modugarden.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NicknameIsDuplicatedResponseDto {
    private Boolean isDuplicated;
}
