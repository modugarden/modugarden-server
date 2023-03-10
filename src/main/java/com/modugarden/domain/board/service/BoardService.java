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
import com.modugarden.domain.fcm.entity.FcmToken;
import com.modugarden.domain.fcm.repository.FcmRepository;
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
import java.util.stream.Collectors;

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
    private final FcmRepository fcmRepository;
    //????????? ??????
    @Transactional
    public BoardCreateResponseDto createBoard(BoardCreateRequestDto boardCreateRequestDto, List<MultipartFile> file, ModugardenUser user) throws IOException {
        //?????? ?????? ??????
        if (file.isEmpty())
            throw new IOException(new BusinessException(ErrorMessage.WRONG_BOARD_FILE));

        InterestCategory interestCategory = interestCategoryRepository.findByCategory(boardCreateRequestDto.getCategory()).get();
        // ?????? board ??? ?????????
        Board board= Board.builder().build();

        boolean index= false;
        for(MultipartFile multipartFile : file) {
            //????????? ????????? url ?????????
            String profileImageUrl = fileService.uploadFile(multipartFile, user.getUserId(), "boardImage");
            //?????? ?????? board ?????? - ????????? ????????? ?????? ??????
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
//        ?????? ?????? ??????????????? ??????!
        if(user.getUser().getAuthority().equals(UserAuthority.ROLE_GENERAL)) {
            User currentUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
            currentUser.changeAuthority(UserAuthority.ROLE_CURATOR);
        }
        return new BoardCreateResponseDto(boardRepository.save(board).getId());
    }

    //????????? ????????? ??????
    @Transactional
    public BoardLikeResponseDto createLikeBoard(Long board_id, ModugardenUser user) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        if (likeBoardRepository.findByUserAndBoard(user.getUser(), board).isEmpty()) {
            board.addLike(); // ?????? ?????? ??????
            likeBoardRepository.save(new LikeBoard(user.getUser(), board));
        }else{
            throw new BusinessException(ErrorMessage.ALREADY_LIKED_BOARD); // ?????? ????????????????????? ??????, ?????? ??????
        }

        return new BoardLikeResponseDto(board.getId(), board.getLike_num());
    }

    //????????? ??????
    public BoardStorageResponseDto storeBoard(ModugardenUser user, Long board_id) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        if(boardStorageRepository.findByUserAndBoard(user.getUser(),board).isPresent())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_STORAGE);

        BoardStorage boardStorage = new BoardStorage(user.getUser(), board);
        boardStorageRepository.save(boardStorage);
        return new BoardStorageResponseDto(boardStorage.getUser().getId(),boardStorage.getBoard().getId());
    }

    //????????? ?????? ?????? api
    public BoardGetResponseDto getBoard(long id, ModugardenUser user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        List<BoardImage> imageList = boardImageRepository.findAllByBoard(board);
        List<FcmToken> fcmTokens = fcmRepository.findByUser(board.getUser());
        List<String> result = fcmTokens.stream().map(fcm -> fcm.getFcmToken()).collect(Collectors.toList());
        return new BoardGetResponseDto(board,imageList,likeBoardRepository.findByUserAndBoard(user.getUser(), board).isPresent(),boardStorageRepository.findByUserAndBoard(user.getUser(), board).isPresent(),followRepository.exists(user.getUserId(), board.getUser().getId()), result);
    }

    //?????? ????????? ??????
    public Slice<BoardUserGetResponseDto> getUserBoard(long user_id, Pageable pageable) {
        Slice<Board> imageList = boardRepository.findAllByUser_IdOrderByCreatedDateDesc(user_id,pageable);

        if (imageList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return imageList
                .map(BoardUserGetResponseDto::new);
    }

    //????????? ??????
    public Slice<BoardSearchResponseDto> searchBoard(User user, String title, Pageable pageable) {
        Slice<Board> board = boardRepository.querySearchBoard('%' + title + '%', pageable, user.getId());

        if (board.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return board.map(BoardSearchResponseDto::new);
    }

    //????????????,????????? ????????? ??????
    public Slice<BoardSearchResponseDto> getFeed(User user, String category, Pageable pageable) {
        InterestCategory interestCategory = interestCategoryRepository.findByCategory(category).get();
        Slice<Board> getFeedBoardList = boardRepository.querySearchBoardByCategory(user.getId(),interestCategory, pageable);

        if (getFeedBoardList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return getFeedBoardList.map(BoardSearchResponseDto::new);
    }

    //????????? ????????? ?????? ??????
    public BoardLikeResponseDto getLikeBoard(long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        return new BoardLikeResponseDto(board.getId(), board.getLike_num());
    }

    //??? ????????? ????????? ?????? api
    public Slice<BoardMyProfileGetResponseDto> getMyBoard(long user_id, Pageable pageable) {
        Slice<Board> postList = boardRepository.findAllByUser_IdOrderByCreatedDateDesc(user_id, pageable);
        if (postList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return postList
                .map(BoardMyProfileGetResponseDto::new);
    }

    //??? ????????? ????????? ????????? ?????? api
    public BoardGetMyLikeResponseDto getMyLikeBoard(long board_id, ModugardenUser users) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        if(likeBoardRepository.findByUserAndBoard(users.getUser(), board).isPresent())
            return new BoardGetMyLikeResponseDto(users.getUserId(),board.getId(), true);

        return new BoardGetMyLikeResponseDto(users.getUserId(),board.getId(), false);
    }

    //??? ????????? ????????? ????????? ??????
    public Slice<BoardGetStorageResponseDto> getStorageBoard(long user_id, Pageable pageable) {
        Slice<Board> boardStorageList = boardRepository.QueryfindAllByUser_Id(user_id, pageable);

        if (boardStorageList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_BOARD_LIST);

        return boardStorageList.map(
                BoardGetStorageResponseDto::new
        );
    }


    //??? ????????? ????????? ?????? ?????? ?????? api
    public BoardGetMyStorageResponseDto getMyStorageBoard(long board_id, ModugardenUser users) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        if(boardStorageRepository.findByUserAndBoard(users.getUser(), board).isPresent())
            return new BoardGetMyStorageResponseDto(users.getUserId(),board.getId(), true);

        return new BoardGetMyStorageResponseDto(users.getUserId(),board.getId(), false);
    }

    //????????? ??????
    @Transactional
    public BoardDeleteResponseDto deleteBoard(long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD_DELETE));

        if (board.getUser().getId().equals(user.getId())) {
            //????????? ?????? ??????
            List<BoardImage> boardImageList = boardImageRepository.findAllByBoard(board);
            for (BoardImage boardImage : boardImageList) {
                fileService.deleteFile(boardImage.getImage());
            }

            boardImageRepository.deleteAllByBoard(board);
            // ?????? ?????? ??????
            boardStorageRepository.deleteAllByBoard(board);
            // ????????? ?????? ??????
            likeBoardRepository.deleteAllByBoard(board);
            // ?????? ??????
            commentRepository.deleteAllByBoard(board);
            // ?????? ?????? ??????
            reportBoardRepository.deleteAllByReportBoard(board);
            boardRepository.delete(board);
        }
        else
            throw new BusinessException(ErrorMessage.WRONG_BOARD_DELETE);

        return new BoardDeleteResponseDto(board.getId());
    }

    //????????? ????????? ??????
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

    //????????? ?????? ??????
    public BoardStorageResponseDto storeCancelBoard(ModugardenUser user, Long board_id) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));

        boardStorageRepository.findByUserAndBoard(user.getUser(),board).ifPresent(
                boardStorageRepository::delete
        );

        return new BoardStorageResponseDto(board.getUser().getId(), board.getId());
    }

    //????????? ?????? ??????
    public Slice<BoardFollowFeedResponseDto> getFollowFeed(ModugardenUser user, Pageable pageable){
        List<Long> userList = followRepository.listFindByFollowingUser_Id(user.getUserId(),pageable);
        userList.add(user.getUserId());

        Slice<Board> boardSlice = boardRepository.findBoard(userList,pageable);

        return boardSlice
                .map(b -> new BoardFollowFeedResponseDto(b,boardImageRepository.findAllByBoard(b),likeBoardRepository.findByUserAndBoard(user.getUser(), b).isPresent(),boardStorageRepository.findByUserAndBoard(user.getUser(), b).isPresent(), fcmRepository.findByUser(b.getUser()).stream().map(fcm -> fcm.getFcmToken()).collect(Collectors.toList())));
    }

    // ?????? ????????? ?????? ????????? ??????
    @Transactional
    public void deleteAllBoardOfUser(User user){
        // ????????? ????????? ?????? ????????? ??????
        List<Board> allBoardOfUser = boardRepository.findByUser(user);
        for (Board board : allBoardOfUser) {
            deleteBoard(board.getId(), user);
        }
        boardRepository.flush();

        likeBoardRepository.deleteByUser(user); // ????????? ????????? ????????? ??????
        likeBoardRepository.flush();
        boardStorageRepository.deleteByUser(user); // ????????? ????????? ?????? ??????
        boardStorageRepository.flush();
    }

}
