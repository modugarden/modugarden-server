package com.modugarden.domain.comment.repository;

import com.modugarden.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //댓글 조회
    Slice<Comment> findByBoardId(Long boardId, Pageable pageable);
    //댓글 삭제
    void deleteBy(Comment comment);
}
