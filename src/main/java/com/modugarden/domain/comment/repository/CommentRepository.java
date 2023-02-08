package com.modugarden.domain.comment.repository;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommentRepository extends JpaRepository <Comment, Long> {
    //댓글 조회
    Slice<Comment> findAllByBoard_IdOrderByCreatedDateAsc(@Param("board_Id") Long boardId, Pageable pageable);
    Optional<Comment> deleteAllByBoard(Board board);

    void deleteByParentId(Long parentId);

    @Query("select c.id from Comment c where c.user = :user")
    List<Long> findAllCommentIdByUser(@Param("user")User user);

    @Modifying(clearAutomatically = true, flushAutomatically = true) // 벌크 연산, 영속성 컨텍스트 초기화까지
    @Query("delete from Comment c where c.parentId in :parentIds")
    int blukDeleteByParentIds(@Param("parentIds")List<Long> parentIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true) // 벌크 연산, 영속성 컨텍스트 초기화까지
    @Query("delete from Comment c where c.id in :commentIds")
    int blukDeleteByCommentIds(@Param("commentIds")List<Long> commentIds); // 벌크 연산 반환값은 int, Integer, void 타입만 가능. int, Integer인 경우 삭제된 행의 개수 반환.
}