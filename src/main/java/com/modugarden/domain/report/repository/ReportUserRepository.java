package com.modugarden.domain.report.repository;

import com.modugarden.domain.report.entity.UserReport;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportUserRepository extends JpaRepository<UserReport, Long> {

    Long deleteByUser(User user);
    Boolean existsByUserAndReportUser(User user, User reportUser);
}
