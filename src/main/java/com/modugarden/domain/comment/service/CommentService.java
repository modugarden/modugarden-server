package com.modugarden.domain.comment.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.comment.dto.request.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.response.CommentCreateResponseDto;
import com.modugarden.domain.comment.dto.response.CommentListResponseDto;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 조회
    //부모 댓글로 조회후 부모댓글이 같다면 시간순으로 조회
    public Slice<CommentListResponseDto> commentList(Long boardId, User user, Pageable pageable){
        Slice<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedDateDesc(boardId, pageable);
        Slice<CommentListResponseDto> result = comments
                .map(c -> new CommentListResponseDto(c.getUser().getId(), c.getUser().getNickname(), c.getUser().getProfileImg(), c.getContent()));  //commentId를 가져와야 하나?
        return result;
    }
    //댓글, 대댓글 작성
    @Transactional
    public CommentCreateResponseDto write(User user, CommentCreateRequestDto dto){
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_POST));
        Comment newComment = new Comment(dto.getContent(), board, user);
        commentRepository.save(newComment);
        return new CommentCreateResponseDto(newComment.getCommentId(), newComment.getParentId());
    }
//    //댓글 삭제
//    public CommentCreateResponseDto delete(User user){
//        CommentCreateRequestDto dto = new CommentCreateRequestDto();
//        boardRepository.findById(dto.getBoardId());
//        Comment deleteComment = commentRepository.findById(dto.getParentId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_POST));
//        commentRepository.delete(deleteComment);
//        return new CommentCreateResponseDto(deleteComment.getCommentId());
//    }
    //댓글 삭제2
    @Transactional
    public CommentCreateResponseDto delete2(User user){
        CommentCreateRequestDto dto = new CommentCreateRequestDto();
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_POST));
        Comment deleteComment = new Comment(dto.getContent(), board, user);
        commentRepository.delete(deleteComment);
        return new CommentCreateResponseDto(deleteComment.getCommentId(), deleteComment.getCommentId());
    }
}
