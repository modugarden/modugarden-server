package com.modugarden.domain.comment.repository;

import com.modugarden.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> , CustomCommentRepository {
    //댓글 조회
    Slice<Comment> findAllByCommentIdOrderByCreatedDateDesc(@Param("commentId")Long commentId, Pageable pageable);
    //comment 가져오기
    List<String> readComment(String comment);    //동해 오빠꺼
    Optional<Comment> deleteAllByBoard_Id(Long id);

}
