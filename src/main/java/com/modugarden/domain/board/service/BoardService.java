package com.modugarden.domain.board.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.s3.FileService;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.block.repository.BlockRepository;
import com.modugarden.domain.board.dto.request.BoardCreateRequestDto;
import com.modugarden.domain.board.dto.response.*;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import com.modugarden.domain.board.repository.BoardImageRepository;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;

import com.modugarden.domain.comment.repository.CommentRepository;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.like.entity.LikeBoard;
import com.modugarden.domain.like.repository.LikeBoardRepository;
import com.modugarden.domain.report.repository.ReportBoardRepository;
import com.modugarden.domain.storage.entity.BoardStorage;
import com.modugarden.domain.storage.repository.BoardStorageRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.entity.enums.UserAuthority;
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
    private final ReportBoardRepository reportBoardRepository;
    private final BlockRepository blockRepository;
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
                        .like_num(0L)
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
//        회원 권한 큐레이터로 변경!
        if(user.getUser().getAuthority().equals(UserAuthority.ROLE_GENERAL)) {
            User currentUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
            currentUser.changeAuthority(UserAuthority.ROLE_CURATOR);
        }
        return new BoardCreateResponseDto(boardRepository.save(board).getId());
    }

    //포스트 좋아요 달기
    @Transactional
    public BoardLikeResponseDto createLikeBoard(Long board_id, ModugardenUser user) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        if (likeBoardRepository.findByUserAndBoard(user.getUser(), board).isEmpty()) {
            board.addLike(); // 더티 체킹 사용
            likeBoardRepository.save(new LikeBoard(user.getUser(), board));
        }else{
            throw new BusinessException(ErrorMessage.ALREADY_LIKED_BOARD); // 이미 좋아요되어있는 경우, 에러 반환
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
        List<BoardImage> imageList = boardImageRepository.findAllByBoard(board);

        return new BoardGetResponseDto(board,imageList,likeBoardRepository.findByUserAndBoard(user.getUser(), board).isPresent(),boardStorageRepository.findByUserAndBoard(user.getUser(), board).isPresent(),followRepository.exists(user.getUserId(), board.getUser().getId()));
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
        Slice<Board> board = boardRepository.querySearchBoard('%' + title + '%', pageable);

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


    //내 프로필 포스트 보관 여부 조회 api
    public BoardGetMyStorageResponseDto getMyStorageBoard(long board_id, ModugardenUser users) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        if(boardStorageRepository.findByUserAndBoard(users.getUser(), board).isPresent())
            return new BoardGetMyStorageResponseDto(users.getUserId(),board.getId(), true);

        return new BoardGetMyStorageResponseDto(users.getUserId(),board.getId(), false);
    }

    //포스트 삭제
    @Transactional
    public BoardDeleteResponseDto deleteBoard(long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD_DELETE));

        if (board.getUser().getId().equals(user.getId())) {
            //이미지 모두 삭제
            boardImageRepository.deleteAllByBoard(board);
            // 보관 모두 삭제
            boardStorageRepository.deleteAllByBoard(board);
            // 좋아요 모두 삭제
            likeBoardRepository.deleteAllByBoard(board);
            // 댓글 삭제
            commentRepository.deleteAllByBoard(board);
            // 신고 모두 삭제
            reportBoardRepository.deleteAllByReportBoard(board);
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

        likeBoardRepository.findByUserAndBoard(user.getUser(), board)
                .ifPresent(it -> {
                    board.delLike();
                    likeBoardRepository.delete(it);
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
        userList.add(user.getUserId());

        Slice<Board> boardSlice = boardRepository.findBoard(userList,pageable);

        return boardSlice
                .map(b -> new BoardFollowFeedResponseDto(b,boardImageRepository.findAllByBoard(b),likeBoardRepository.findByUserAndBoard(user.getUser(), b).isPresent(),boardStorageRepository.findByUserAndBoard(user.getUser(), b).isPresent()));
    }

    // 해당 유저의 모든 포스트 삭제
    @Transactional
    public void deleteAllBoardOfUser(User user){
        // 유저가 작성한 모든 포스트 삭제
        List<Board> allBoardOfUser = boardRepository.findByUser(user);
        for (Board board : allBoardOfUser) {
            deleteBoard(board.getId(), user);
        }
        boardRepository.flush();

        likeBoardRepository.deleteByUser(user); // 유저의 포스트 좋아요 삭제
        likeBoardRepository.flush();
        boardStorageRepository.deleteByUser(user); // 유저의 포스트 저장 삭제
        boardStorageRepository.flush();
    }

}
