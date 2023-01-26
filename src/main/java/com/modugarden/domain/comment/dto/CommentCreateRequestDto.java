package com.modugarden.domain.comment.dto;

import com.modugarden.domain.comment.entity.Comment;
import lombok.*;


@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    private String content;
    //validation 할 때 글자 수 제한 물어보기
    private Long parentId;
    private Long boardId;
    private Comment reportedComment;  //@NotNull로 유효성 검사를 해야 하는가?
}
