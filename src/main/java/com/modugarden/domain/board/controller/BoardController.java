package com.modugarden.domain.board.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.board.dto.request.BoardCreateRequestDto;
import com.modugarden.domain.board.dto.response.*;
import com.modugarden.domain.board.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@EnableJpaAuditing
public class BoardController {

    private final BoardService boardService;

    //포스트 작성 api
    @ApiOperation( value= "업로드 페이지 - 포스트 작성", notes = "사용자가 포스트를 사진과 함께 작성한다.")
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
    public BaseResponseDto<BoardLikeResponseDto> createLikeBoard(@PathVariable Long board_id,
                                                                                                          @AuthenticationPrincipal ModugardenUser user) {
        BoardLikeResponseDto boardLikeResponse = boardService.createLikeBoard(board_id, user);
        return new BaseResponseDto<>(boardLikeResponse);
    }

    //포스트 보관 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 큐레이션 보관", notes = "큐레이션을 보관한다.")
    @PostMapping("/boards/{board_id}/storage")
    public BaseResponseDto<BoardStorageResponseDto> storeBoard(@PathVariable Long board_id, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(boardService.storeBoard(user, board_id));
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
    public SliceResponseDto<BoardUserGetResponseDto> getUserBoard(@PathVariable Long user_id, Pageable pageable) {
        return new SliceResponseDto<>(boardService.getUserBoard(user_id, pageable));
    }

    //제목 포스트 검색 api
    @ApiOperation(value = "탐색 피드 - 제목 포스트 검색", notes = "제목 으로 포스트 검색")
    @GetMapping("/boards/search")
    public SliceResponseDto<BoardSearchResponseDto> searchBoard(@RequestParam @Size(max=50) String title, Pageable pageable) {
        return new SliceResponseDto<>(boardService.searchBoard(title, pageable));
    }

    //카테고리,날짜별 큐레이션 조회 api
    @ApiOperation(value = "탐색 피드 - 카테고리 별 포스트 검색", notes = "카테고리별로 포스트 검색")
    @GetMapping("/boards")
    public SliceResponseDto<BoardSearchResponseDto> getFeedBoard(@RequestParam String category, Pageable pageable) {
        return new SliceResponseDto<>(boardService.getFeed(category, pageable));
    }


    //포스트 좋아요 개수 조회 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 포스트 좋아요 조회", notes = "특정 포스트의 좋아요 조회한다.")
    @GetMapping("/boards/like/{boards_id}")
    public BaseResponseDto<BoardLikeResponseDto> getLikeBoard(@PathVariable Long boards_id) {
        return new BaseResponseDto<>(boardService.getLikeBoard(boards_id));
    }

    //내 프로필 포스트 조회 api
    @ApiOperation(value = "프로필 페이지 - 포스트 조회", notes = "로그인한 사용자의 포스트를 조회한다.")
    @GetMapping("/boards/me")
    public SliceResponseDto<BoardUserGetResponseDto> getMyBoard(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(boardService.getMyBoard(user.getUserId(), pageable));
    }

    //내 프로필 큐레이션 좋아요 여부 조회
    @ApiOperation(value = "프로필 페이지 - 포스트 좋아요 여부 조회", notes = "특정 포스트 좋아요 여부 조회한다.")
    @GetMapping("/boards/me/like/{board_id}")
    public BaseResponseDto<BoardGetMyLikeResponseDto> getMyLikeBoard(@PathVariable Long board_id,
                                                                     @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(boardService.getMyLikeBoard(board_id, user));
    }

    //내 프로필 저장한 포스트 조회
    @ApiOperation(value = "프로필 페이지 - 저장한 포스트 조회", notes = "로그인한 사용자의 저장 포스트를 조회한다.")
    @GetMapping("/boards/me/storage")
    public SliceResponseDto<BoardGetStorageResponseDto> getStorageBoard(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(boardService.getStorageBoard(user.getUserId(), pageable));
    }

    //내 프로필 포스트 보관 여부 조회
    @ApiOperation(value = "프로필 페이지 - 큐레이션 보관 여부 조회", notes = "특정 큐레이션 보관 여부 조회한다.")
    @GetMapping("/boards/me/storage/{board_id}")
    public BaseResponseDto<BoardGetMyStorageResponseDto> getMyStorageBoard(@PathVariable Long board_id,
                                                                                 @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(boardService.getMyStorageBoard(board_id, user));
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
    @DeleteMapping("/boards/{boㅂard_id}/unlike")
    public BaseResponseDto<BoardLikeResponseDto> createUnlikeBoard(@PathVariable Long board_id,
                                                                      @AuthenticationPrincipal ModugardenUser user) {
        BoardLikeResponseDto boardLikeResponseDto = boardService.createUnlikeBoard(board_id, user);
        return new BaseResponseDto<>(boardLikeResponseDto);
    }

    //포스트 보관 취소 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 포스트 보관 취소", notes = "보관된 포스트를 취소한다.")
    @DeleteMapping("/boards/{board_id}/storage")
    public BaseResponseDto<BoardStorageResponseDto> storeCancelBoard(@PathVariable Long board_id, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(boardService.storeCancelBoard(user, board_id));
    }
}
