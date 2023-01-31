package com.modugarden.domain.report.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.report.dto.response.ReportBoardResponseDto;
import com.modugarden.domain.report.dto.response.ReportCommentResponseDto;
import com.modugarden.domain.report.dto.response.ReportCurationResponseDto;
import com.modugarden.domain.report.dto.response.ReportUserResponseDto;
import com.modugarden.domain.report.service.ReportService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "남의 프로필 - 메인 - 신고", notes = "남의 프로필 - 메인에서 회원을 신고한다.")
    @PostMapping("/users/{userId}")
    public BaseResponseDto<ReportUserResponseDto> reportUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(reportService.reportUser(user.getUser(), userId));
    }

    @ApiOperation(value = "포스트 상세보기 - 댓글 - 신고", notes = "포스트 상세보기 - 댓글에서 댓글을 신고한다.")
    @PostMapping("/comments/{commentId}")
    public BaseResponseDto<ReportCommentResponseDto> reportComment(@PathVariable Long commentId, @RequestParam("type") String type
                                                                    , @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(reportService.reportComment(user.getUser(), commentId, type));
    }

    @ApiOperation(value = "큐레이션 상세보기 - 메인 - 신고", notes = "큐레이션 상세보기 - 메인에서 댓글을 신고한다.")
    @PostMapping("/curations/{curationId}")
    public BaseResponseDto<ReportCurationResponseDto> reportCuration(@PathVariable Long curationId, @RequestParam("type") String type
                                                                    , @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(reportService.reportCuration(user.getUser(), curationId, type));
    }

    @ApiOperation(value = "포스트 상세보기 - 메인 - 신고", notes = "포스트 상세보기 - 메인에서 댓글을 신고한다.")
    @PostMapping("/boards/{boardId")
    public BaseResponseDto<ReportBoardResponseDto> reportBoard(@PathVariable Long boardId, @RequestParam("type") String type
                                                                , @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(reportService.reportBoard(user.getUser(), boardId, type));
    }
}
