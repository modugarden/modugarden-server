package com.modugarden.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IsEmailDuplicatedResponseDto {
    private boolean duplicate;
}
