package com.modugarden.domain.report.repository;

import com.modugarden.domain.report.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportUserRepository extends JpaRepository<UserReport, Long> {
}
