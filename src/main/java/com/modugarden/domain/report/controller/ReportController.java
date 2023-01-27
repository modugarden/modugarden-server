package com.modugarden.domain.report.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.comment.dto.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.CommentCreateResponseDto;
import com.modugarden.domain.report.dto.response.ReportCommentResponseDto;
import com.modugarden.domain.report.dto.response.ReportUserResponseDto;
import com.modugarden.domain.report.entity.enums.ReportType;
import com.modugarden.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/users/{userId}")
    public BaseResponseDto<ReportUserResponseDto> reportUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(reportService.reportUser(user.getUser(), userId));
    }
    //댓글 신고
    @PostMapping("/comments/{commentId}")
    public BaseResponseDto<ReportCommentResponseDto> reportComment(@PathVariable Long commentId, @RequestParam("report") ReportType report, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(reportService.reportComment(user.getUser(), commentId, report));
    }
}
