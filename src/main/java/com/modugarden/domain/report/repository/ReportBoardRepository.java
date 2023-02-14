package com.modugarden.domain.report.repository;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.report.entity.BoardReport;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportBoardRepository extends JpaRepository<BoardReport, Long> {

    Optional<BoardReport> deleteAllByReportBoard(Board board);
    Boolean existsByUserAndReportBoard(User user, Board board);
}
