package com.modugarden.domain.comment.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.comment.dto.CommentRequestDto;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.comment.service.CommentService;
import com.modugarden.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
public class CommentController {

    private CommentService commentService;

    // 댓글 조회
    // 이걸 보통 보드컨트롤러에서 같이 하던데,,흠
    @GetMapping("/boards/{board_id}/comments")
    @Inject
    public @ResponseBody BaseResponseDto commentList(@RequestParam("boardId") long boardId) {
        List<Comment> comments = null;
        comments = commentService.list(boardId);
        return commentList(boardId);
    }

    // 댓글 작성
    @PostMapping("/boards/{board_id}/comments")
    public ResponseEntity write(@PathVariable User userId, @RequestBody CommentRequestDto dto, Comment comment) {
        commentService.write(userId, dto);
        return ResponseEntity.ok(commentService.write(userId, dto));
    }

    // 댓글 삭제
    @PatchMapping("/boards/{board_id}/comments/{comment_id}")
    public ResponseEntity delete(@PathVariable User userId) {
        commentService.delete(userId);
        return ResponseEntity.ok(userId);
    }

}
