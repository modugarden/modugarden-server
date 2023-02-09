package com.modugarden.domain.report.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.board.entity.Board;
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
public class BoardReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board reportBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public BoardReport(ReportType reportType, Board reportBoard, User user) {
        this.reportType = reportType;
        this.reportBoard = reportBoard;
        this.user = user;
    }
}
