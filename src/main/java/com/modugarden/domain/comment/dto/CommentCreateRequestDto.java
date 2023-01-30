package com.modugarden.domain.comment.dto;

import com.modugarden.domain.comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.Size;


@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    private Long commentId;
    @Size(max= 40)
    private String content;
    private Long parentId;
    private Long boardId;
}
