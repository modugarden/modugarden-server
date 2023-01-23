package com.modugarden.domain.comment.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    //댓글 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    //내용
    @Column(nullable = false)
    private String content;
    //validation 할 때 글자 수 제한 물어보기
    //부모댓글 id
    private Long parentId;
    //포스트 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board boardId;
    //회원 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;
}
