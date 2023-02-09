package com.modugarden.domain.board.repository;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
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

    Slice<Board> findAllByUser_IdOrderByCreatedDateDesc(Long user_id, Pageable pageable);

    //제목 검색
    @Query(value = "SELECT bo FROM Board bo \n" +
            "            LEFT OUTER JOIN UserBlock bs ON bo.user.id = bs.blockUser.id\n" +
            "            WHERE bs.user.id is null and bo.title like :title"+
            "           order by bo.createdDate desc")
    Slice<Board> querySearchBoard(String title, Pageable pageable);


    //카테고리로 생성일자 순 조회
    Slice<Board> findAllByCategoryOrderByCreatedDateDesc(InterestCategory category, Pageable pageable);

    @Query(value = "SELECT bo FROM Board bo \n" +
            "            LEFT JOIN BoardStorage bs ON bo.id = bs.board.id\n" +
            "            WHERE :user_id = bs.user.id"+
            "           order by bs.createdDate desc")
    Slice<Board> QueryfindAllByUser_Id(@Param("user_id") Long user_id, Pageable pageable);

    @Query("select b from Board b inner join b.user u where u.id in :followingUserIds order by b.createdDate desc")
    Slice<Board> findBoard(List<Long> followingUserIds, Pageable pageable);

    Long countByUser_Id(Long user_id);

    List<Board> findByUser(User user);
}
