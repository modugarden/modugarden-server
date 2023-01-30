package com.modugarden.domain.comment.dto;

import com.modugarden.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CommentListResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private String comment;
}
