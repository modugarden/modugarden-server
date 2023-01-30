package com.modugarden.domain.board.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.s3.FileService;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.board.dto.request.BoardCreateImageReqeuestDto;
import com.modugarden.domain.board.dto.request.BoardCreateRequestDto;
import com.modugarden.domain.board.dto.request.BoardLikeRequestDto;
import com.modugarden.domain.board.dto.response.*;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import com.modugarden.domain.board.repository.BoardImageRepository;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;

import com.modugarden.domain.curation.dto.response.CurationLikeResponseDto;
import com.modugarden.domain.curation.dto.response.CurationStorageResponseDto;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.like.repository.LikeBoardRepository;
import com.modugarden.domain.storage.entity.BoardStorage;
import com.modugarden.domain.storage.entity.CurationStorage;
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


    //포스트 생성
    @Transactional
    public BoardCreateResponseDto createBoard(BoardCreateRequestDto boardCreateRequestDto, List<MultipartFile> file, ModugardenUser user) throws IOException {

        if (file.isEmpty())
            throw new IOException(new BusinessException(ErrorMessage.WRONG_CURATION_FILE));

        InterestCategory interestCategory = interestCategoryRepository.findByCategory(boardCreateRequestDto.getCategory()).get();

        Board board = Board.builder()
                .title(boardCreateRequestDto.getTitle())
                .location(boardCreateRequestDto.getLocation())
                .like_num((long)0)
                .user(user.getUser())
                .category(interestCategory)
                .build();

        boardRepository.save(board);

        for(MultipartFile multipartFile : file) {
            String profileImageUrl = fileService.uploadFile(multipartFile, user.getUserId(), "boardImage");
            BoardCreateImageReqeuestDto boardCreateImageReqeuestDto = new BoardCreateImageReqeuestDto(profileImageUrl, boardCreateRequestDto.getContent().get(file.indexOf(multipartFile)),user.getUserId(), board);
            boardImageRepository.save(boardCreateImageReqeuestDto.toEntity());
        }

        return new BoardCreateResponseDto(boardRepository.save(board).getId());
    }

    //포스트 좋아요 달기
    @Transactional
    public BoardLikeResponseDto createLikeBoard(Long board_id, ModugardenUser user) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        User users = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        if (likeBoardRepository.findByUserAndBoard(users, board).isEmpty()) {
            Board modifyBoard = new Board(board.getId(), board.getTitle(), board.getLike_num()+1, board.getLocation(), board.getUser(),board.getCategory());
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
    public BoardGetResponseDto getBoard(long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        List<BoardImage> imageList = boardImageRepository.findAllByBoard_Id(id);
        return new BoardGetResponseDto(board,imageList);
    }

    //회원 포스트 조회
    public Slice<BoardUserGetResponseDto> getUserCuration(long user_id, Pageable pageable) {
        Slice<BoardImage> imageList = boardImageRepository.findAllByUserid(user_id,pageable);

        return imageList.map(BoardUserGetResponseDto::new);
    }

    //포스트 좋아요 개수 조회
    public BoardLikeResponseDto getLikeBoard(long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        return new BoardLikeResponseDto(board.getId(), board.getLike_num());
    }

    //포스트 삭제
    @Transactional
    public BoardDeleteResponseDto deleteBoard(long id, ModugardenUser user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION_DELETE));

        if (board.getUser().getId().equals(user.getUserId())) {
            //이미지 모두 삭제
            boardImageRepository.deleteAllByBoard_Id(id);
            // 보관 모두 삭제
//            curationStorageRepository.deleteAllByCuration_Id(curation.getId());
            // 좋아요 모두 삭제
//            likeRepository.deleteAllByCuration_Id(curation.getId());
            boardRepository.delete(board);
        }
        else
            throw new BusinessException(ErrorMessage.WRONG_BOARD_DELETE);

        return new BoardDeleteResponseDto(board.getId());
    }

    //큐레이션 좋아요 취소
    @Transactional
    public BoardLikeResponseDto createUnlikeBoard(Long board_id, ModugardenUser user) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_BOARD));
        User users = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        likeBoardRepository.findByUserAndBoard(users, board)
                .ifPresent(it -> {
                    Board modifyBoard = new Board(board.getId(), board.getTitle(), board.getLike_num()-1, board.getLocation(), board.getUser(),board.getCategory());
                    likeBoardRepository.delete(it);
                    boardRepository.save(modifyBoard);
                });

        return new BoardLikeResponseDto(board.getId(), board.getLike_num());
    }

}
