package com.modugarden.domain.report.repository;

import com.modugarden.domain.report.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportBoardRepository extends JpaRepository<BoardReport, Long> {
}
