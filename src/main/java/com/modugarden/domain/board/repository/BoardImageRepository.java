package com.modugarden.domain.board.repository;

import com.modugarden.domain.board.dto.response.BoardGetStorageResponseDto;
import com.modugarden.domain.board.entity.BoardImage;
import org.springframework.data.domain.Page;
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


    @Query(value = "SELECT bo FROM BoardImage bo \n" +
            "            LEFT JOIN BoardStorage bs ON bo.userid = bs.user.id\n" +
            "            WHERE bo.userid = bs.user.id")
    Page<BoardGetStorageResponseDto> QueryfindAllByUser_Id(Long user_id, Pageable pageable);
}
