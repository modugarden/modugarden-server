package com.modugarden.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeleteRequestDto {
    private Long commentId;
}
