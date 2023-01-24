package com.modugarden.domain.comment.dto;

import lombok.*;


@Getter
public class CommentCreateRequestDto {
    private String content;
    //validation 할 때 글자 수 제한 물어보기
    private Long parentId;
    private Long boardId;
}
