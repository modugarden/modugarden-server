package com.modugarden.domain.report.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.report.dto.response.ReportUserResponseDto;
import com.modugarden.domain.report.entity.UserReport;
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
@Transactional(readOnly = true)
public class ReportService {

    private final ReportUserRepository reportUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReportUserResponseDto reportUser(User user, Long reportUserId) {
        User reportUser = userRepository.findById(reportUserId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        UserReport userReport = new UserReport(user, reportUser);
        reportUserRepository.save(userReport);
        return new ReportUserResponseDto(userReport.getUser().getId(), userReport.getReportUser().getId());
    }
}
