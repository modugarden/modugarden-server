package com.modugarden.domain.board.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.s3.FileService;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.board.dto.request.BoardCreateRequestDto;
import com.modugarden.domain.board.dto.request.BoardLikeRequestDto;
import com.modugarden.domain.board.dto.response.*;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import com.modugarden.domain.board.repository.BoardImageRepository;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;

import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.like.repository.LikeBoardRepository;
import com.modugarden.domain.storage.entity.BoardStorage;
import com.modugarden.domain.storage.entity.repository.BoardStorageRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final LikeBoardRepository likeBoardRepository;
    private final UserRepository userRepository;
    private final BoardStorageRepository boardStorageRepository;
    private final InterestCategoryRepository interestCategoryRepository;
    private final FileService fileService;
    private final FollowRepository followRepository;
    private final CommentRepository commentRepository;

    //포스트 생성
    @Transactional
    public BoardCreateResponseDto createBoard(BoardCreateRequestDto boardCreateRequestDto, List<MultipartFile> file, ModugardenUser user) throws IOException {
        //사진 유무 조회
        if (file.isEmpty())
            throw new IOException(new BusinessException(ErrorMessage.WRONG_BOARD_FILE));

        InterestCategory interestCategory = interestCategoryRepository.findByCategory(boardCreateRequestDto.getCategory()).get();
        // 처음 board 값 초기화
        Board board= Board.builder().build();

        boolean index= false;
        for(MultipartFile multipartFile : file) {
            //프로필 이미지 url 만들기
            String profileImageUrl = fileService.uploadFile(multipartFile, user.getUserId(), "boardImage");
            //처음 한번 board 생성 - 썸네일 이미지 포함 생성
            if(!index) {
                board = Board.builder()
                        .title(boardCreateRequestDto.getTitle())
                        .like_num((long) 0)
                        .preview_img(profileImageUrl)
                        .user(user.getUser())
                        .category(interestCategory)
                        .build();
                boardRepository.save(board);
                index=true;
            }

            BoardImage boardImage = BoardImage.builder()
                    .image(profileImageUrl)
                    .content(boardCreateRequestDto.getContent().get(file.indexOf(multipartFile)))
                    .location(boardCreateRequestDto.getLocation().get(file.indexOf(multipartFile)))
                    .userid(user.getUserId())
                    .board(board)
                    .build();

            boardImageRepository.save(boardImage);
        }
        return new BoardCreateResponseDto(boardRepository.save(board).getId());
    }

    //포스트 좋아요 달기
    @Transactional
    public BoardLikeResponseDto createLikeBoard(Long board_id, ModugardenUser user) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        User users = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        if (likeBoardRepository.findByUserAndBoard(users, board).isEmpty()) {
            Board modifyBoard = new Board(board.getId(), board.getTitle(), board.getLike_num()+1,board.getPreview_img(),board.getUser(),board.getCategory());
            BoardLikeRequestDto boardLikeRequestDto = new BoardLikeRequestDto(users, modifyBoard);
            likeBoardRepository.save(boardLikeRequestDto.toEntity());
            boardRepository.save(modifyBoard);
        }

        return new BoardLikeResponseDto(board.getId(), board.getLike_num());
    }

    //포스트 보관
    public BoardStorageResponseDto storeBoard(ModugardenUser user, Long board_id) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        if(boardStorageRepository.findByUserAndBoard(user.getUser(),board).isPresent())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_STORAGE);

        BoardStorage boardStorage = new BoardStorage(user.getUser(), board);
        boardStorageRepository.save(boardStorage);
        return new BoardStorageResponseDto(boardStorage.getUser().getId(),boardStorage.getBoard().getId());
    }

    //포스트 하나 조회 api
    public BoardGetResponseDto getBoard(long id, ModugardenUser user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        List<BoardImage> imageList = boardImageRepository.findAllByBoard_Id(id);
        return new BoardGetResponseDto(board,imageList,likeBoardRepository.findByUserAndBoard(user.getUser(), board).isPresent(),boardStorageRepository.findByUserAndBoard(user.getUser(), board).isPresent());
    }

    //회원 포스트 조회
    public Slice<BoardUserGetResponseDto> getUserBoard(long user_id, Pageable pageable) {
        Slice<Board> imageList = boardRepository.findAllByUser_IdOrderByCreatedDateDesc(user_id,pageable);

        if (imageList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return imageList
                .map(BoardUserGetResponseDto::new);
    }

    //포스트 검색
    public Slice<BoardSearchResponseDto> searchBoard(String title, Pageable pageable) {
        Slice<Board> board = boardRepository.findAllByTitleLikeOrderByCreatedDateDesc('%' + title + '%', pageable);
        if (board.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return board.map(BoardSearchResponseDto::new);
    }

    //카테고리,날짜별 포스트 조회
    public Slice<BoardSearchResponseDto> getFeed(String category, Pageable pageable) {
        InterestCategory interestCategory = interestCategoryRepository.findByCategory(category).get();
        Slice<Board> getFeedBoardList = boardRepository.findAllByCategoryOrderByCreatedDateDesc(interestCategory, pageable);

        if (getFeedBoardList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return getFeedBoardList.map(BoardSearchResponseDto::new);
    }

    //포스트 좋아요 개수 조회
    public BoardLikeResponseDto getLikeBoard(long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        return new BoardLikeResponseDto(board.getId(), board.getLike_num());
    }

    //내 프로필 포스트 조회 api
    public Slice<BoardMyProfileGetResponseDto> getMyBoard(long user_id, Pageable pageable) {
        Slice<Board> postList = boardRepository.findAllByUser_IdOrderByCreatedDateDesc(user_id, pageable);
        if (postList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return postList
                .map(BoardMyProfileGetResponseDto::new);
    }

    //내 프로필 포스트 좋아요 조회 api
    public BoardGetMyLikeResponseDto getMyLikeBoard(long board_id, ModugardenUser users) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        if(likeBoardRepository.findByUserAndBoard(users.getUser(), board).isPresent())
            return new BoardGetMyLikeResponseDto(users.getUserId(),board.getId(), true);

        return new BoardGetMyLikeResponseDto(users.getUserId(),board.getId(), false);
    }

    //내 프로필 저장한 포스트 조회
    public Slice<BoardGetStorageResponseDto> getStorageBoard(long user_id, Pageable pageable) {
        Slice<Board> boardStorageList = boardRepository.QueryfindAllByUser_Id(user_id, pageable);

        if (boardStorageList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return boardStorageList.map(
                BoardGetStorageResponseDto::new
        );
    }


    //내 프로필 큐레이션 보관 여부 조회 api
    public BoardGetMyStorageResponseDto getMyStorageBoard(long board_id, ModugardenUser users) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        if(boardStorageRepository.findByUserAndBoard(users.getUser(), board).isPresent())
            return new BoardGetMyStorageResponseDto(users.getUserId(),board.getId(), true);

        return new BoardGetMyStorageResponseDto(users.getUserId(),board.getId(), false);
    }

    //포스트 삭제
    @Transactional
    public BoardDeleteResponseDto deleteBoard(long id, ModugardenUser user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD_DELETE));

        if (board.getUser().getId().equals(user.getUserId())) {
            //이미지 모두 삭제
            boardImageRepository.deleteAllByBoard_Id(id);
            // 보관 모두 삭제
            boardStorageRepository.deleteAllByBoard_Id(id);
            // 좋아요 모두 삭제
            likeBoardRepository.deleteAllByBoard_Id(id);
            // 댓글 삭제
            commentRepository.deleteAllByBoard_Id(id);

            boardRepository.delete(board);
        }
        else
            throw new BusinessException(ErrorMessage.WRONG_BOARD_DELETE);

        return new BoardDeleteResponseDto(board.getId());
    }

    //포스트 좋아요 취소
    @Transactional
    public BoardLikeResponseDto createUnlikeBoard(Long board_id, ModugardenUser user) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        User users = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        likeBoardRepository.findByUserAndBoard(users, board)
                .ifPresent(it -> {
                    Board modifyBoard = new Board(board.getId(), board.getTitle(), board.getLike_num()-1,board.getPreview_img(), board.getUser(),board.getCategory());
                    likeBoardRepository.delete(it);
                    boardRepository.save(modifyBoard);
                });

        return new BoardLikeResponseDto(board.getId(), board.getLike_num());
    }

    //포스트 보관 취소
    public BoardStorageResponseDto storeCancelBoard(ModugardenUser user, Long board_id) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        boardStorageRepository.findByUserAndBoard(user.getUser(),board).ifPresent(
                boardStorageRepository::delete
        );

        return new BoardStorageResponseDto(board.getUser().getId(), board.getId());
    }

    //팔로우 피드 조회
    public Slice<BoardFollowFeedResponseDto> getFollowFeed(ModugardenUser user, Pageable pageable){
        List<Long> userList = followRepository.listFindByFollowingUser_Id(user.getUserId(),pageable);
        Slice<Board> boardSlice = boardRepository.findBoard(userList,pageable);

        return boardSlice
                .map(u -> new BoardFollowFeedResponseDto(u,boardImageRepository.findAllByBoard_Id(u.getId()),likeBoardRepository.findByUserAndBoard(user.getUser(), u).isPresent(),boardStorageRepository.findByUserAndBoard(user.getUser(), u).isPresent()));
    }

}
