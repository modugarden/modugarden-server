package com.modugarden.domain.report.repository;

import com.modugarden.domain.report.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportBoardRepository extends JpaRepository<BoardReport, Long> {

    Optional<BoardReport> deleteAllByReportBoard_Id(Long BoardId);
}
