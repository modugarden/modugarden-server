package com.modugarden.domain.comment.repository;

import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> , CustomCommentRepository {
    //댓글 조회
    Slice<Comment> findByBoard_Id(@Param("boardId")Long boardId, Pageable pageable);
}
