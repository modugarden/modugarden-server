package com.modugarden.domain.comment.service;

import com.modugarden.domain.comment.dto.CommentRequestDto;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    //댓글 조회
    public List<Comment> list(long boardId){
        return commentRepository.list(boardId);
    }
    //댓글 작성
    public Long write(User userId, CommentRequestDto dto){
        dto.setUserId(userId);
        Comment comment = dto.toEntity();
        commentRepository.save(comment);
        commentRepository.write(comment);
        return dto.getCommentId();
    }
    //댓글 삭제
    public void delete(User userId){
        Comment comment = new Comment();
        commentRepository.delete(comment);
    }
}
