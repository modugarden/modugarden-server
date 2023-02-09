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
    private boolean block;  //차단했는지
    private boolean isblocked;  //차단 당했는지
}
