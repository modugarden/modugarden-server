package com.modugarden.domain.report.repository;

import com.modugarden.domain.report.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportCommentRepository extends JpaRepository<CommentReport, Long> {
}
