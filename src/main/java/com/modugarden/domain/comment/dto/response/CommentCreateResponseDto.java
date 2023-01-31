package com.modugarden.domain.comment.dto.response;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentCreateResponseDto {
    private Long commentId;
}
