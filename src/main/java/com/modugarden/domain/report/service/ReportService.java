package com.modugarden.domain.report.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.curation.repository.CurationRepository;
import com.modugarden.domain.report.dto.response.ReportBoardResponseDto;
import com.modugarden.domain.report.dto.response.ReportCommentResponseDto;
import com.modugarden.domain.report.dto.response.ReportCurationResponseDto;
import com.modugarden.domain.report.dto.response.ReportUserResponseDto;
import com.modugarden.domain.report.entity.BoardReport;
import com.modugarden.domain.report.entity.CommentReport;
import com.modugarden.domain.report.entity.CurationReport;
import com.modugarden.domain.report.entity.UserReport;
import com.modugarden.domain.report.entity.enums.ReportType;
import com.modugarden.domain.report.repository.ReportBoardRepository;
import com.modugarden.domain.report.repository.ReportCommentRepository;
import com.modugarden.domain.report.repository.ReportCurationRepository;
import com.modugarden.domain.report.repository.ReportUserRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReportService {

    private final ReportUserRepository reportUserRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReportCommentRepository reportCommentRepository;
    private final CurationRepository curationRepository;
    private final ReportCurationRepository reportCurationRepository;
    private final BoardRepository boardRepository;
    private final ReportBoardRepository reportBoardRepository;


    public ReportUserResponseDto reportUser(User user, Long reportUserId) {
        User reportUser = userRepository.findById(reportUserId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        if(reportUserRepository.existsByUserAndReportUser(user,reportUser))
            throw new BusinessException(ErrorMessage.ALREADY_REPORT_USER);
        UserReport userReport = new UserReport(user, reportUser);
        reportUserRepository.save(userReport);
        return new ReportUserResponseDto(userReport.getUser().getId(), userReport.getReportUser().getId());
    }

    public ReportCommentResponseDto reportComment(User user, Long reportCommentId, String type) {
        Comment reportComment = commentRepository.findById(reportCommentId).orElseThrow(() -> new BusinessException(ErrorMessage.COMMENT_NOT_FOUND));
        if(reportCommentRepository.existsByUserAndReportComment(user, reportComment))
            throw new BusinessException(ErrorMessage.ALREADY_REPORT_COMMENT);
        ReportType reportType = ReportType.from(type);
        CommentReport commentReport = new CommentReport(reportType, reportComment, user);
        reportCommentRepository.save(commentReport);
        return new ReportCommentResponseDto(commentReport.getUser().getId(), commentReport.getReportComment().getId());
    }

    public ReportCurationResponseDto reportCuration(User user, Long curationId, String type) {
        Curation reportCuration = curationRepository.findById(curationId).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        if(reportCurationRepository.existsByUserAndReportCuration(user, reportCuration))
            throw new BusinessException(ErrorMessage.ALREADY_REPORT_CURATION);
        ReportType reportType = ReportType.from(type);
        CurationReport curationReport = new CurationReport(reportType,reportCuration,user);
        reportCurationRepository.save(curationReport);
        return new ReportCurationResponseDto(curationReport.getUser().getId(), curationReport.getReportCuration().getId());
    }

    public ReportBoardResponseDto reportBoard(User user, Long boardId, String type) {
        Board reportBoard = boardRepository.findById(boardId).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        if(reportBoardRepository.existsByUserAndReportBoard(user, reportBoard))
            throw new BusinessException(ErrorMessage.ALREADY_REPORT_BOARD);
        ReportType reportType = ReportType.from(type);
        BoardReport boardReport = new BoardReport(reportType, reportBoard, user);
        reportBoardRepository.save(boardReport);
        return new ReportBoardResponseDto(boardReport.getUser().getId(), boardReport.getReportBoard().getId());
    }
}
