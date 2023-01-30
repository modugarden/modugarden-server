package com.modugarden.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentListResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private String comment;
}
