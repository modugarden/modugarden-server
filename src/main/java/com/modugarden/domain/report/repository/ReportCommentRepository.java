package com.modugarden.domain.report.repository;

import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.report.entity.CommentReport;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportCommentRepository extends JpaRepository<CommentReport, Long> {

    Boolean existsByUserAndReportComment(User user, Comment reportComment);
}
