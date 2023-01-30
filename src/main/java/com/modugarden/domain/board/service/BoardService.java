package com.modugarden.domain.board.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.s3.FileService;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.board.dto.request.BoardCreateImageReqeuestDto;
import com.modugarden.domain.board.dto.request.BoardCreateRequestDto;
import com.modugarden.domain.board.dto.response.BoardCreateResponseDto;
import com.modugarden.domain.board.dto.response.BoardGetResponseDto;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import com.modugarden.domain.board.repository.BoardImageRepository;
import com.modugarden.domain.board.repository.BoardRepository;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.category.repository.InterestCategoryRepository;

import lombok.RequiredArgsConstructor;
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
                .user(user.getUser())
                .category(interestCategory)
                .build();

        boardRepository.save(board);

        for(MultipartFile multipartFile : file) {
            String profileImageUrl = fileService.uploadFile(multipartFile, user.getUserId(), "boardImage");
            BoardCreateImageReqeuestDto boardCreateImageReqeuestDto = new BoardCreateImageReqeuestDto(profileImageUrl, boardCreateRequestDto.getContent().get(file.indexOf(multipartFile)), board);
            boardImageRepository.save(boardCreateImageReqeuestDto.toEntity());
        }

        return new BoardCreateResponseDto(boardRepository.save(board).getId());
    }

    //포스트 하나 조회 api
    public BoardGetResponseDto getBoard(long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        List<BoardImage> imageList = boardImageRepository.findAllByBoard_Id(id);
        return new BoardGetResponseDto(board,imageList);
    }
}
