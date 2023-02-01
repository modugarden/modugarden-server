package com.modugarden.domain.board.repository;

import com.modugarden.domain.board.dto.response.BoardGetStorageResponseDto;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import com.modugarden.domain.category.entity.InterestCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long boardId);

    Long countByUser_Id(Long user_id);

    //제목 검색
    Slice<Board> findAllByTitleLikeOrderByCreatedDateDesc(String title, Pageable pageable);

    //카테고리로 생성일자 순 조회
    Slice<Board> findAllByCategoryOrderByCreatedDateDesc(InterestCategory category, Pageable pageable);

    @Query(value = "SELECT bo FROM Board bo \n" +
            "            LEFT JOIN BoardStorage bs ON bo.id = bs.board.id\n" +
            "            WHERE bo.user.id = bs.user.id")
    Slice<BoardGetStorageResponseDto> QueryfindAllByUser_Id(Long user_id, Pageable pageable);

    @Query("select b from Board b inner join b.user u where u.id in :followingUserIds order by b.createdDate")
    Slice<Board> findBoard(List<Long> followingUserIds, Pageable pageable);
}
