package com.modugarden.domain.block.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnBlockUserResponseDto {

    private Long userId;
    private Long unBlockUserId;
}
