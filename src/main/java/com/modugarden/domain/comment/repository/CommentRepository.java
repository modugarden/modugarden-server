package com.modugarden.domain.comment.repository;

import com.modugarden.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //댓글 조회
    List<Comment> list(long boardId);
    //댓글 작성
    void write(Comment comment);
    //댓글 삭제
    void delete(Comment comment);
}
