package com.modugarden.domain.comment.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.comment.dto.request.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.request.CommentDeleteRequestDto;
import com.modugarden.domain.comment.dto.response.CommentCreateResponseDto;
import com.modugarden.domain.comment.dto.response.CommentDeleteResponseDto;
import com.modugarden.domain.comment.dto.response.CommentListResponseDto;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 조회
    public Slice<CommentListResponseDto> commentList(Long boardId, User user, Pageable pageable){
        Slice<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedDateAsc(boardId, pageable);
        Slice<CommentListResponseDto> result = comments
                .map(c -> new CommentListResponseDto(c.getUser().getId(), c.getUser().getNickname(), c.getUser().getProfileImg(), c.getContent(), c.getCommentId(), c.getParentId(), c.getCreatedDate()));
        return result;
    }
    //댓글 작성
    @Transactional
    public CommentCreateResponseDto write(User user, Long boardId, CommentCreateRequestDto dto) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        Comment newComment;
        if (dto.getParentId()==null) { // 부모 댓글 작성, null 값 들어올 수 있음
            newComment = new Comment(dto.getContent(), null, board, user); // parentId에 일단 아무값이나 채우기(DB에서 not null 조건 있어서)
            commentRepository.save(newComment); // newComment.getCommentId -> 자동생성된 값이 있음.
        } else {
            commentRepository.findById(dto.getParentId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_PARENT_COMMENT_ID));// 존재하는 부모댓글인지 확인
            newComment = new Comment(dto.getContent(), dto.getParentId(), board, user);
            commentRepository.save(newComment);
        }
        Comment comment = commentRepository.findById(newComment.getCommentId()).orElseThrow(()-> new BusinessException(ErrorMessage.COMMENT_NOT_FOUND));
        return new CommentCreateResponseDto(comment.getUser().getId(), comment.getUser().getNickname(), comment.getUser().getProfileImg(), dto.getContent(), newComment.getCommentId(), dto.getParentId(), comment.getCreatedDate());
    }

    //댓글 삭제
    @Transactional
    public CommentDeleteResponseDto delete(User user, CommentDeleteRequestDto dto){
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_COMMENT));
        if(comment.getParentId()==null) {
            commentRepository.deleteByParentId(comment.getCommentId());
        }
        commentRepository.deleteById(comment.getCommentId());
        return new CommentDeleteResponseDto(dto.getCommentId());
    }

    public void deleteAllCommentOfUser(User user) {
        List<Long> allCommentIdByUser = commentRepository.findAllCommentIdByUser(user);
        int count1 = commentRepository.blukDeleteByParentIds(allCommentIdByUser);
        int count2 = commentRepository.blukDeleteByCommentIds(allCommentIdByUser);
    }
}