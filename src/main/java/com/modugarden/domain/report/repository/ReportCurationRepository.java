package com.modugarden.domain.report.repository;

import com.modugarden.domain.report.entity.CurationReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportCurationRepository extends JpaRepository<CurationReport, Long> {
}
