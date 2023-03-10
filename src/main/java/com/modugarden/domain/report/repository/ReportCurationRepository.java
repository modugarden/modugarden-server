package com.modugarden.domain.report.repository;

import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.report.entity.CurationReport;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportCurationRepository extends JpaRepository<CurationReport, Long> {

    Optional<CurationReport> deleteAllByReportCuration(Curation curation);
    Boolean existsByUserAndReportCuration(User user, Curation reportCuration);
}
