package com.modugarden.domain.curation.controller;

import com.modugarden.common.response.BaseResponseDto;

import com.modugarden.common.response.PageResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.curation.dto.request.CurationCreateRequestDto;
import com.modugarden.domain.curation.dto.response.*;
import com.modugarden.domain.curation.service.CurationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@EnableJpaAuditing
public class CurationController {

    private final CurationService curationService;

    //큐레이션 작성 api
    @ApiOperation(value = "업로드 페이지 - 큐레이션 작성", notes = "사용자가 큐레이션을 사진과 함께 작성한다.")
    @PostMapping(value = "/curations", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponseDto<CurationCreateResponseDto> createCuration(@RequestPart @Valid CurationCreateRequestDto curationCreateRequest,
                                                                     @AuthenticationPrincipal ModugardenUser user,
                                                                     @RequestPart MultipartFile file) throws IOException {
        CurationCreateResponseDto curationCreateResponse = curationService.createCuration(curationCreateRequest, file, user);
        return new BaseResponseDto<>(curationCreateResponse);
    }

    //큐레이션 좋아요 달기 api
    @PostMapping("/curations/{curation_id}/like")
    public BaseResponseDto<CurationLikeResponseDto> createLikeCuration(@PathVariable Long curation_id,
                                                                       @AuthenticationPrincipal ModugardenUser user) {
        CurationLikeResponseDto curationLikeResponse = curationService.createLikes(curation_id, user);
        return new BaseResponseDto<>(curationLikeResponse);
    }

    //큐레이션 보관 api
    @PostMapping("/curations/{curation_id}/storage")
    public BaseResponseDto<CurationStorageResponseDto> storeCuration(@PathVariable Long curation_id, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(curationService.storeCuration(user, curation_id));
    }

    //큐레이션 하나 조회 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 큐레이션 하나 조회", notes = "특정 큐레이션 한개를 조회 한다.")
    @GetMapping("/curations/{curation_id}")
    public BaseResponseDto<CurationGetResponseDto> getCuration(@PathVariable Long curation_id) {
        return new BaseResponseDto<>(curationService.getCuration(curation_id));
    }

    //회원 큐레이션 조회 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 회원 큐레이션 조회", notes = "특정 회원의 모든 큐레이션을 조회 한다.")
    @GetMapping("/curations/users/{user_id}")
    public SliceResponseDto<CurationUserGetResponseDto> getUserCuration(@PathVariable Long user_id, Pageable pageable) {
        return new SliceResponseDto<>(curationService.getUserCuration(user_id, pageable));
    }

    //제목 큐레이션 검색 api
    @ApiOperation(value = "탐색 피드 - 제목 큐레이션 검색", notes = "제목 으로 큐레이션 검색")
    @GetMapping("/curations/search")
    public SliceResponseDto<CurationSearchResponseDto> searchCuration(@RequestParam @Size(max=50) String title, Pageable pageable) {
        return new SliceResponseDto<>(curationService.searchCuration(title, pageable));
    }

    //카테고리,날짜별 큐레이션 조회 api
    @GetMapping("/curations")
    public SliceResponseDto<CurationSearchResponseDto> getFeedCuration(@RequestParam @Valid InterestCategory category, Pageable pageable) {
        return new SliceResponseDto<>(curationService.getFeed(category, pageable));
    }

    //큐레이션 좋아요 개수 조회 api
    @GetMapping("/curations/like/{curation_id}")
    public BaseResponseDto<CurationLikeResponseDto> getLikeCuration(@PathVariable Long curation_id) {
        return new BaseResponseDto<>(curationService.getLike(curation_id));
    }

    //내 프로필 큐레이션 조회 api
    @GetMapping("/curations/me")
    public PageResponseDto<CurationUserGetResponseDto> getMyCuration(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new PageResponseDto<>(curationService.getMyCuration(user.getUserId(), pageable));
    }

    //내 프로필 큐레이션 좋아요 여부 조회
    @GetMapping("/curations/me/like/{curation_id}")
    public CurationGetMyLikeResponseDto getMyLikeCuration(@PathVariable Long curation_id,
                                                          @AuthenticationPrincipal ModugardenUser user) {
        return curationService.getMyLikeCuration(curation_id, user);
    }

    //내 프로필 저장한 큐레이션 조회
    @GetMapping("curations/me/storage")
    public PageResponseDto<CurationGetStorageResponseDto> getStorageCuration(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new PageResponseDto<>(curationService.getStorageCuration(user.getUserId(), pageable));
    }

    //큐레이션 삭제 api
    @ApiOperation(value = "게시물 상세보기 페이지 - 큐레이션 삭제", notes = "사용자의 큐레이션을 삭제한다.")
    @DeleteMapping("/curations/{curation_id}")
    public BaseResponseDto<CurationDeleteResponseDto> deleteCuration(@PathVariable Long curation_id,
                                                                     @AuthenticationPrincipal ModugardenUser user) {
        CurationDeleteResponseDto curationDeleteResponse = curationService.delete(curation_id,user);
        return new BaseResponseDto<>(curationDeleteResponse);
    }

    //큐레이션 좋아요 취소 api
    @DeleteMapping("/curations/{curation_id}/unlike")
    public BaseResponseDto<CurationLikeResponseDto> createUnlikeCuration(@PathVariable Long curation_id,
                                                                       @AuthenticationPrincipal ModugardenUser user) {
        CurationLikeResponseDto curationLikeResponse = curationService.createUnlikes(curation_id, user);
        return new BaseResponseDto<>(curationLikeResponse);
    }

    //큐레이션 보관 취소 api
    @DeleteMapping("/curations/{curation_id}/storage")
    public BaseResponseDto<CurationStorageResponseDto> storeCancelCuration(@PathVariable Long curation_id, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(curationService.storeCancelCuration(user, curation_id));
    }
}
