package com.modugarden.domain.comment.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.comment.dto.request.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.request.CommentDeleteRequestDto;
import com.modugarden.domain.comment.dto.response.CommentCreateResponseDto;
import com.modugarden.domain.comment.dto.response.CommentDeleteResponseDto;
import com.modugarden.domain.comment.dto.response.CommentListResponseDto;
import com.modugarden.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// 컨트롤러에서 서비스 호출, 서비스에서 레퍼지토리를 호출
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class CommentController {
    private final CommentService commentService;

    // 댓글 전체 조회
    @GetMapping("/{board_id}/comments")
    public SliceResponseDto<CommentListResponseDto> commentList(@PathVariable Long board_id, @AuthenticationPrincipal ModugardenUser modugardenUser, Pageable pageable) {
        return new SliceResponseDto<>(commentService.commentList(board_id, modugardenUser.getUser(), pageable));
    }

    // 댓글, 대댓글 작성
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @PostMapping("/{board_id}/comments")
    public BaseResponseDto<CommentCreateResponseDto> write(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody @Valid CommentCreateRequestDto dto, @PathVariable Long board_id) {
        return new BaseResponseDto<>(commentService.write(modugardenUser.getUser(), board_id, dto));
    }

    // 댓글 삭제
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @DeleteMapping("/{board_id}/comments")
    public BaseResponseDto<CommentDeleteResponseDto> delete(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody CommentDeleteRequestDto dto, @PathVariable Long board_id) {
        return new BaseResponseDto<>(commentService.delete(modugardenUser.getUser(), dto));
    }
}