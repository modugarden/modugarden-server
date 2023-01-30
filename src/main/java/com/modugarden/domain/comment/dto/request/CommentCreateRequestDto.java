package com.modugarden.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    @NotBlank(message = "댓글은 필수 입력 값입니다.")
    @Size(max= 40)
    private String content;
    private Long boardId;
}
