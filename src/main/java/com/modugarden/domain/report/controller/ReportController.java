package com.modugarden.domain.report.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.report.dto.response.ReportUserResponseDto;
import com.modugarden.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/users/{userId}")
    public BaseResponseDto<ReportUserResponseDto> reportUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(reportService.reportUser(user.getUser(), userId));
    }
}
