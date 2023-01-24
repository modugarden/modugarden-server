package com.modugarden.domain.comment.dto;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.user.entity.User;
import lombok.Getter;

@Getter
public class CommentReponseDto {
    private Long commentId;
    private String content;
    //validation 할 때 글자 수 제한 물어보기
    private Long parentId;
    private Board boardId;
    private User userId;

    public void CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content= comment.getContent();
        this.parentId= comment.getParentId();
        this.boardId=comment.getBoardId();
        this.userId=comment.getUserId();
    }
}
