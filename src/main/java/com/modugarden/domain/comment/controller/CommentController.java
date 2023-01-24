package com.modugarden.domain.comment.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.comment.dto.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.CommentCreateResponseDto;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

// 컨트롤러에서 서비스 호출, 서비스에서 레퍼지토리를 호출
@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class CommentController {

    private final  CommentService commentService;

    // 댓글 조회
    // 이걸 보통 보드컨트롤러에서 같이 하던데,,흠
//    @GetMapping("/{board_id}/comments")
//    @Inject
//    public @ResponseBody BaseResponseDto commentList(@RequestParam("boardId") long boardId) {
//        List<Comment> comments = null;
//        comments = commentService.list(boardId);
//        return commentList(boardId);
//    }

    // 댓글 작성
    @PostMapping("/{board_id}/comments")
    public BaseResponseDto<CommentCreateResponseDto> write(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody CommentCreateRequestDto dto) {
        CommentCreateResponseDto responseDto = commentService.write(modugardenUser.getUser(), dto);
        return new BaseResponseDto<>(responseDto);
    }

    // 댓글 삭제
//    @PatchMapping("/{board_id}/comments/{comment_id}")
//    public BaseResponseDto<CommentCreateResponseDto> delete(@AuthenticationPrincipal ModugardenUser modugardenUser) {
//        commentService.delete(userId);
//        CommentCreateResponseDto responseDto = commentService.delete(modugardenUser.getUser(), dto);
//        return ResponseEntity.ok(userId);
//    }

}
