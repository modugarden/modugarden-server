package com.modugarden.domain.comment.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.comment.dto.CommentCreateRequestDto;
import com.modugarden.domain.comment.dto.CommentCreateResponseDto;
import com.modugarden.domain.comment.dto.CommentListResponseDto;
import com.modugarden.domain.comment.entity.Comment;
import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.modugarden.common.error.enums.ErrorMessage.WRONG_PARENT_COMMENT_ID;
import static com.modugarden.common.error.enums.ErrorMessage.WRONG_POST;

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
    public CommentCreateResponseDto write(User user, Long boardId, CommentCreateRequestDto dto){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BusinessException(WRONG_POST));
        Comment newComment;

        if(dto.getParentId() == null){ // 부모 댓글 작성
            newComment = new Comment(dto.getContent(), 0L , board, user); // parentId에 일단 아무값이나 채우기(DB에서 not null 조건 있어서)
            commentRepository.save(newComment);// newComment.getCommentId -> 자동생성된 값이 있음.
            newComment.updateParentIdOfParentComment();
        }else{
            commentRepository.findById(dto.getParentId()).orElseThrow(() -> new BusinessException(WRONG_PARENT_COMMENT_ID));// 존재하는 부모댓글인지 확인

            newComment = new Comment(dto.getContent(), dto.getParentId(), board, user);
            commentRepository.save(newComment);
        }
        return new CommentCreateResponseDto(newComment.getCommentId());
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
 /*   @Transactional
    public CommentCreateResponseDto delete2(User user){
        CommentCreateRequestDto dto = new CommentCreateRequestDto();
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(() -> new BusinessException(WRONG_POST));
        Comment deleteComment = new Comment(dto.getContent(), dto.getParentId(), board, user);
        commentRepository.delete(deleteComment);
        return new CommentCreateResponseDto(deleteComment.getCommentId());
    }*/
}
