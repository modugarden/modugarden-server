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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 조회
    public Slice<CommentListResponseDto> readCommentList(Long boardId, User user, Pageable pageable){
        Slice<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedDateAsc(boardId, pageable);
        Slice<CommentListResponseDto> result = comments
                .map(c -> new CommentListResponseDto(c.getUser().getId(), c.getUser().getNickname(), c.getUser().getProfileImg(), c.getContent(), c.getId(), c.getParentId(), c.getCreatedDate()));

        return result;
    }

    //댓글 작성
    @Transactional
    public CommentCreateResponseDto write(User user, Long boardId, CommentCreateRequestDto dto) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        Comment newComment;
        if (dto.getParentId()==null) { // 부모 댓글 작성, null 값 들어올 수 있음
            newComment = new Comment(dto.getContent(), null, board, user); // 부모 댓글인 경우, parentId = null
            commentRepository.save(newComment);
        } else {
            commentRepository.findById(dto.getParentId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_PARENT_COMMENT_ID)); // 존재하는 부모댓글인지 확인
            newComment = new Comment(dto.getContent(), dto.getParentId(), board, user);
            commentRepository.save(newComment);
        }

        return new CommentCreateResponseDto(newComment.getUser().getId(), newComment.getUser().getNickname(), newComment.getUser().getProfileImg(), dto.getContent(), newComment.getId(), dto.getParentId(), newComment.getCreatedDate());
    }

    //댓글 삭제
    @Transactional
    public CommentDeleteResponseDto delete(User user, CommentDeleteRequestDto dto){
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_COMMENT));

        if(comment.getParentId()==null) {// 부모 댓글인 경우
            commentRepository.deleteByParentId(comment.getId()); // 부모 댓글아래 달린 자식 댓글들 모두 삭제
        }

        commentRepository.deleteById(comment.getId()); // 삭제 요청들어온 댓글 삭제

        return new CommentDeleteResponseDto(dto.getCommentId());
    }

    // 유저 탈퇴시 해당 유저가 작성한 모든 댓글 삭제
    @Transactional
    public void deleteAllCommentOfUser(User user) {
        List<Long> allCommentIdByUser = commentRepository.findAllCommentIdByUser(user); // 유저가 작성한 모든 댓글 Id들
        commentRepository.blukDeleteByParentIds(allCommentIdByUser); // 유저가 작성한 부모 댓글아래 달린 자식 댓글들 벌크 삭제 연산
        commentRepository.blukDeleteByCommentIds(allCommentIdByUser); // 유저가 작성한 댓글들 벌크 삭제 연산
    }
}