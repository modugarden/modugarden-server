package com.modugarden.domain.comment.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.comment.dto.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.CommentCreateResponseDto;
import com.modugarden.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 컨트롤러에서 서비스 호출, 서비스에서 레퍼지토리를 호출
@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    // 이걸 보통 보드컨트롤러에서 같이 하던데,,흠
    @GetMapping("/{board_id}/comments")
    public SliceResponseDto<CommentCreateResponseDto> commentList(@PathVariable Long boardId, @AuthenticationPrincipal ModugardenUser modugardenUser, Pageable pageable) {
        return new SliceResponseDto<>(commentService.commentList(boardId, modugardenUser.getUser(), pageable));
    }

    // 댓글 작성
    @PostMapping("/{board_id}/comments")
    public BaseResponseDto<CommentCreateResponseDto> write(@AuthenticationPrincipal ModugardenUser modugardenUser, @RequestBody CommentCreateRequestDto dto) {
        CommentCreateResponseDto responseDto = commentService.write(modugardenUser.getUser(), dto);
        return new BaseResponseDto<>(responseDto);
    }

    // 댓글 삭제
    @PatchMapping("/{board_id}/comments/{comment_id}")
    public BaseResponseDto<CommentCreateResponseDto> delete(@AuthenticationPrincipal ModugardenUser modugardenUser) {
        commentService.delete(modugardenUser.getUser());
        return new BaseResponseDto<>(commentService.delete(modugardenUser.getUser()));
    }
    // 댓글 신고
    // 일단 신고하면 삭제되게 다른 방법 뭐 있을지 물어보고 추가
    @PostMapping("/{board_id}/comments/report/{comment_id}")
    public BaseResponseDto<CommentCreateResponseDto>report(@PathVariable Long commentId, @AuthenticationPrincipal ModugardenUser modugardenUser, CommentCreateRequestDto commentCreateRequestDto){
        commentService.delete(modugardenUser.getUser()); //일단 신고하면 댓글도 삭제되어야 하겠지?
        return new BaseResponseDto<>(commentService.delete(modugardenUser.getUser()));
    }
    //대댓글 작성
}
