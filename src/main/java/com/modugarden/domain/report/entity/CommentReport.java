package com.modugarden.domain.report.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.report.entity.enums.ReportType;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CommentReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment reportComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CommentReport(ReportType type, Comment reportComment, User user) {
        this.type = type;
        this.reportComment = reportComment;
        this.user = user;
    }
}
