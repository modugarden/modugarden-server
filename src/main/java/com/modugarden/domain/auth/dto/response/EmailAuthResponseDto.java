package com.modugarden.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmailAuthResponseDto {
    private String authCode;
}
