package com.modugarden.domain.curation.controller;

import com.modugarden.common.response.BaseResponse;

import com.modugarden.domain.curation.dto.CurationCreateRequestDto;
import com.modugarden.domain.curation.dto.CurationCreateResponseDto;
import com.modugarden.domain.curation.service.CurationService;
import com.modugarden.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class CurationController {
    @Autowired
    private CurationService curationService;

    @PostMapping(value = "/curations", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<CurationCreateResponseDto> createCuration(@RequestPart CurationCreateRequestDto curationCreateRequest,
//                                                            @AuthenticationPrincipal User user,
                                                            @RequestPart MultipartFile file) throws IOException {
        //AuthenticationPrincipal을 테스트 못함. 추후 테스트 예정

        CurationCreateResponseDto curationCreateResponse = curationService.save(curationCreateRequest,file);
        return new BaseResponse<>(curationCreateResponse);
    }
}
