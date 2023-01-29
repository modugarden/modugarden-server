package com.modugarden.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class CommentListResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
}
