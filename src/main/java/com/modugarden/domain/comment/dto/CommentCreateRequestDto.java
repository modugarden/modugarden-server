package com.modugarden.domain.comment.dto;

import com.modugarden.domain.comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.Size;


@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    @Size(max= 40)
    private String content;
    private Long parentId; // null이면 부모인겨
}
