package com.modugarden.domain.comment.dto;

import com.modugarden.domain.comment.entity.Comment;
import lombok.*;


@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    private Long commentId;
    private String content;
    private Long parentId;
    private Long boardId;
}
