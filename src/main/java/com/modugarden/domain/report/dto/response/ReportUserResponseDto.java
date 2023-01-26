package com.modugarden.domain.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReportUserResponseDto {
    private Long userId;
    private Long reportUserId;
}
