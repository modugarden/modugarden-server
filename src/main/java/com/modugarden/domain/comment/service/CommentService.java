package com.modugarden.domain.comment.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.comment.dto.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.CommentCreateResponseDto;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //댓글 조회
/*    public List<Comment> list(long boardId){
        return commentRepository.findByBoardId(boardId);
    }*/
    //댓글 작성
    public CommentCreateResponseDto write(User user, CommentCreateRequestDto dto){
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_POST));

        Comment newComment = new Comment(dto.getContent(), dto.getParentId(), board, user);
        commentRepository.save(newComment);

        return new CommentCreateResponseDto(newComment.getCommentId());

    }
    //댓글 삭제
/*    public void delete(User userId){
        Comment comment = new Comment();
        commentRepository.delete(comment);
    }*/
}
