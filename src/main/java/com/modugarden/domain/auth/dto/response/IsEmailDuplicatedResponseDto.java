package com.modugarden.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IsEmailDuplicatedResponseDto {
    private boolean duplicate;
}
