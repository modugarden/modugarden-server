package com.modugarden.domain.comment.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class Comment extends BaseTimeEntity {

    //댓글 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    //내용
    @Column(nullable = false, length = 40)
    private String content;
    //validation 할 때 글자 수 제한 물어보기
    //부모댓글 id
    private Long parentId;
    //포스트 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
    //회원 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Builder
    public Comment(String content, Long parentId, Board board, User user) {
        this.content = content;
        this.parentId = parentId;
        this.board = board;
        this.user = user;
    }

    public void updateParentIdOfParentComment(){
        this.parentId = this.commentId;
    }

}
