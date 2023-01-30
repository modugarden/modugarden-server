package com.modugarden.domain.board.repository;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findAllByBoard_Id(Long board_id);
    Slice<BoardImage> findAllByUserid(Long user_id, Pageable pageable);
}
