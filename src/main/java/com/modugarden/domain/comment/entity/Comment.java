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
    private Board boardId;
    //회원 id
    //신고자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User userId;

    //신고당한 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment reportedComment;
    public void comentReport(User reporterId, Comment reportedComment, String content) {
        this.userId = userId;
        this.reportedComment = reportedComment;
        this.content = content;
    }
    @Builder
    public Comment(String content, Long parentId, Board boardId, User userId) {
        this.content = content;
        this.parentId = parentId;
        this.boardId = boardId;
        this.userId = userId;
    }

}
