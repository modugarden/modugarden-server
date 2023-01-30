package com.modugarden.domain.board.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.board.dto.request.BoardCreateRequestDto;
import com.modugarden.domain.board.dto.response.*;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.service.BoardService;
import com.modugarden.domain.curation.dto.response.CurationLikeResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@EnableJpaAuditing
public class BoardController {

    private final BoardService boardService;

    //포스트 작성 api
    @ApiOperation(value = "업로드 페이지 - 포스트 작성", notes = "사용자가 포스트를 사진과 함께 작성한다.")
    @PostMapping(value = "/boards", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<BoardCreateResponseDto> createBoard(@RequestPart @Valid BoardCreateRequestDto boardCreateRequest,
                                                                  @AuthenticationPrincipal ModugardenUser user,
                                                                  @RequestPart List<MultipartFile> file) throws IOException {
        BoardCreateResponseDto boardCreateResponseDto = boardService.createBoard(boardCreateRequest, file, user);
        return new BaseResponseDto<>(boardCreateResponseDto);
    }

    //포스트 좋아요 달기 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 포스트 좋아요 달기", notes = "특정 포스트에 좋아요 누른다.")
    @PostMapping("/boards/{board_id}/like")
    public BaseResponseDto<com.modugarden.domain.board.dto.response.BoardLikeResponseDto> createLikeBoard(@PathVariable Long board_id,
                                                                                                          @AuthenticationPrincipal ModugardenUser user) {
        com.modugarden.domain.board.dto.response.BoardLikeResponseDto boardLikeResponse = boardService.createLikeBoard(board_id, user);
        return new BaseResponseDto<>(boardLikeResponse);
    }

    //포스트 하나 조회 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 포스트 하나 조회", notes = "특정 포스트 한개를 조회 한다.")
    @GetMapping("/boards/{board_id}")
    public BaseResponseDto<BoardGetResponseDto> getBoard(@PathVariable Long board_id) {
        return new BaseResponseDto<>(boardService.getBoard(board_id));
    }

    //회원 포스트 조회 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 회원 포스트 조회", notes = "특정 회원의 모든 포스트를 조회 한다.")
    @GetMapping("/boards/users/{user_id}")
    public SliceResponseDto<BoardUserGetResponseDto> getUserCuration(@PathVariable Long user_id, Pageable pageable) {
        return new SliceResponseDto<>(boardService.getUserCuration(user_id, pageable));
    }

    //포스트 좋아요 개수 조회 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 포스트 좋아요 조회", notes = "특정 포스트의 좋아요 조회한다.")
    @GetMapping("/boards/like/{boards_id}")
    public BaseResponseDto<BoardLikeResponseDto> getLikeBoard(@PathVariable Long boards_id) {
        return new BaseResponseDto<>(boardService.getLikeBoard(boards_id));
    }

    //포스트 삭제 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 포스트 삭제", notes = "사용자의 포스트를 삭제한다.")
    @DeleteMapping("/boards/{board_id}")
    public BaseResponseDto<BoardDeleteResponseDto> deleteBoard(@PathVariable Long board_id,
                                                                  @AuthenticationPrincipal ModugardenUser user) {
        BoardDeleteResponseDto boardDeleteResponseDto = boardService.deleteBoard(board_id,user);
        return new BaseResponseDto<>(boardDeleteResponseDto);
    }

    //포스트 좋아요 취소 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 포스트 좋아요 취소", notes = "특정 포스트에 좋아요 취소한다.")
    @DeleteMapping("/boards/{board_id}/unlike")
    public BaseResponseDto<BoardLikeResponseDto> createUnlikeBoard(@PathVariable Long board_id,
                                                                      @AuthenticationPrincipal ModugardenUser user) {
        BoardLikeResponseDto boardLikeResponseDto = boardService.createUnlikeBoard(board_id, user);
        return new BaseResponseDto<>(boardLikeResponseDto);
    }

}
