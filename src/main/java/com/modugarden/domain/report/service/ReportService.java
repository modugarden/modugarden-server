package com.modugarden.domain.report.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.report.dto.response.ReportCommentResponseDto;
import com.modugarden.domain.report.dto.response.ReportUserResponseDto;
import com.modugarden.domain.report.entity.CommentReport;
import com.modugarden.domain.report.entity.UserReport;
import com.modugarden.domain.report.repository.ReportCommentRepository;
import com.modugarden.domain.report.repository.ReportUserRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.modugarden.domain.report.entity.QUserReport.userReport;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReportService {

    private final ReportUserRepository reportUserRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReportCommentRepository reportCommentRepository;


    public ReportUserResponseDto reportUser(User user, Long reportUserId) {
        User reportUser = userRepository.findById(reportUserId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        UserReport userReport = new UserReport(user, reportUser);
        reportUserRepository.save(userReport);
        return new ReportUserResponseDto(userReport.getUser().getId(), userReport.getReportUser().getId());
    }
    public ReportCommentResponseDto reportComment(User user, Long reportCommentId) {
        Comment reportComment = commentRepository.findById(reportCommentId).orElseThrow(() -> new BusinessException(ErrorMessage.COMMENT_NOT_FOUND));
        CommentReport commentReport = new CommentReport(user, reportComment);
        reportCommentRepository.save(commentReport);
        return new ReportCommentResponseDto(commentReport.getUser().getId(), commentReport.getReportComment().getCommentId());
    }
}
