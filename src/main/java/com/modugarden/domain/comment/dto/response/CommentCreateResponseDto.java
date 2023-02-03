package com.modugarden.domain.comment.dto.response;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CommentCreateResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private String comment;
    private Long commentId;
    private Long parentId;
    private LocalDateTime localDateTime;
}
