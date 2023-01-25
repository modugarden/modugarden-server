package com.modugarden.domain.curation.controller;

import com.modugarden.common.response.BaseResponseDto;

import com.modugarden.common.response.PageResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.curation.dto.*;
import com.modugarden.domain.curation.service.CurationService;
import com.modugarden.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@EnableJpaAuditing
public class CurationController {

    @Autowired
    private CurationService curationService;

    //큐레이션 생성 api
    @PostMapping(value = "/curations", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<CurationCreateResponseDto> createCuration(@RequestPart CurationCreateRequestDto curationCreateRequest,
                                                                     //@AuthenticationPrincipal User user,
                                                                     @RequestPart MultipartFile file) throws IOException {
        //AuthenticationPrincipal을 테스트 못함. 추후 테스트 예정
        CurationCreateResponseDto curationCreateResponse = curationService.save(curationCreateRequest, file);
        return new BaseResponseDto<>(curationCreateResponse);
    }

    //큐레이션 좋아요 달기 api
    @PostMapping("/curations/{curation_id}/like")
    public BaseResponseDto<CurationLikeResponseDto> createCuration(@PathVariable Long curation_id,
                                                                    @AuthenticationPrincipal User user){
        CurationLikeResponseDto curationLikeResponse = curationService.createLikes(curation_id, user.getId());
        return new BaseResponseDto<>(curationLikeResponse);
    }


    //큐레이션 하나 조회 api
    @GetMapping("/curations/{curation_id}")
    public CurationGetResponseDto getCuration(@PathVariable Long curation_id) {
        return curationService.get(curation_id);
    }

    //회원 큐레이션 조회 api
    @GetMapping("/curations/users/{user_id}")
    public PageResponseDto<CurationUserGetResponseDto> getUserCuration(@PathVariable Long user_id, Pageable pageable) {
        return new PageResponseDto<>(curationService.getUser(user_id, pageable));
    }
    //카테고리, 제목별 큐레이션 검색 api
    @GetMapping("/curations/search")
    public SliceResponseDto<CurationSearchResponseDto> searchCuration(@RequestParam @Valid InterestCategory category, @RequestParam @Valid String title, Pageable pageable){
        return new SliceResponseDto<>(curationService.search(category,title, pageable));
    }

    //카테고리,날짜별 큐레이션 조회 api
    @GetMapping("/curations")
    public SliceResponseDto<CurationSearchResponseDto> getFeedCuration(@RequestParam @Valid InterestCategory category, Pageable pageable){
        return new SliceResponseDto<>(curationService.getFeed(category, pageable));
    }



    //큐레이션 삭제
    @DeleteMapping("/curations/{curation_id}") //me로 수정 필요
    public BaseResponseDto<CurationDeleteResponseDto> deleteCuration(@PathVariable Long curation_id) {
        CurationDeleteResponseDto curationDeleteResponse = curationService.delete(curation_id);
        return new BaseResponseDto<>(curationDeleteResponse);
    }


}
