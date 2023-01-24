package com.modugarden.domain.comment.dto;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private Long commentId;
    private String content;
    //validation 할 때 글자 수 제한 물어보기
    private Long parentId;
    private Board boardId;
    private User userId;
    public Comment toEntity() {
        Comment comments = Comment.builder()
                .commentId(commentId)
                .content(content)
                .userId(userId)
                .parentId(parentId)
                .boardId(boardId)
                .build();

        return comments;
    }
}
