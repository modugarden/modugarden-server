package com.modugarden.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CommentListResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private String comment;
    private Long commentId;
    private Long parentId;
    private LocalDateTime localDateTime;
}
