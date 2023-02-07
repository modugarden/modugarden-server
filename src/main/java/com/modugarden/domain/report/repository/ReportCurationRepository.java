package com.modugarden.domain.report.repository;

import com.modugarden.domain.report.entity.CurationReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportCurationRepository extends JpaRepository<CurationReport, Long> {

    Optional<CurationReport> deleteAllByReportCuration_Id(Long curationId);
}
