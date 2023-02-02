package com.modugarden.domain.board.repository;

import com.modugarden.domain.board.entity.BoardImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findAllByBoard_Id(Long board_id);

    Optional<BoardImage> deleteAllByBoard_Id(Long id);

    Slice<BoardImage> findAllByUserid(Long user_id, Pageable pageable);
}
