package com.modugarden.domain.comment.dto;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.user.entity.User;

import javax.persistence.*;

public class CommentDto {
    private Long commentId;
    private String content;
    //validation 할 때 글자 수 제한 물어보기
    private Long parentId;
    private Board boardId;
    private User userId;
}
