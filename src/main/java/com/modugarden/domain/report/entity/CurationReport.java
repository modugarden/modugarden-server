package com.modugarden.domain.report.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.curation.entity.Curation;
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
public class CurationReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curation_id", nullable = false)
    private Curation reportCuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public CurationReport(ReportType reportType, Curation reportCuration, User user) {
        this.reportType = reportType;
        this.reportCuration = reportCuration;
        this.user = user;
    }
}
