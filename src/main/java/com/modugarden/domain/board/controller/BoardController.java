package com.modugarden.domain.board.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.board.dto.request.BoardCreateRequestDto;
import com.modugarden.domain.board.dto.response.BoardCreateResponseDto;
import com.modugarden.domain.board.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    public BaseResponseDto<BoardCreateResponseDto> createBoard(@RequestPart @Valid BoardCreateRequestDto BoardCreateRequest,
                                                                  @AuthenticationPrincipal ModugardenUser user,
                                                                  @RequestPart List<MultipartFile> file) throws IOException {
        BoardCreateResponseDto boardCreateResponseDto = boardService.createBoard(BoardCreateRequest, file, user);
        return new BaseResponseDto<>(boardCreateResponseDto);
    }
}
