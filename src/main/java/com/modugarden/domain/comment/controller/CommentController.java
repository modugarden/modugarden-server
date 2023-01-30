package com.modugarden.domain.comment.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.comment.dto.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.CommentCreateResponseDto;
import com.modugarden.domain.comment.dto.CommentListResponseDto;
import com.modugarden.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 컨트롤러에서 서비스 호출, 서비스에서 레퍼지토리를 호출
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class CommentController {
    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/{board_id}/comments")
    public SliceResponseDto<CommentListResponseDto> commentList(@PathVariable Long boardId, @AuthenticationPrincipal ModugardenUser modugardenUser, Pageable pageable) {
        return new SliceResponseDto<>(commentService.commentList(boardId, modugardenUser.getUser(), pageable));
    }

    // 댓글, 대댓글 작성
    @PostMapping("/{board_id}/comments")
    public BaseResponseDto<CommentCreateResponseDto> write(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody CommentCreateRequestDto dto, @PathVariable Long board_id) {
        return new BaseResponseDto<>(commentService.write(modugardenUser.getUser(), board_id, dto));
    }

    // 댓글 삭제
/*    @PatchMapping("/{board_id}/comments/{comment_id}")
    public BaseResponseDto<CommentCreateResponseDto> delete(@AuthenticationPrincipal ModugardenUser modugardenUser) {
        return new BaseResponseDto<>(commentService.delete2(modugardenUser.getUser()));
    }*/
}
