package com.modugarden.domain.follow.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.follow.dto.*;
import com.modugarden.domain.follow.service.FollowService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 컨트롤러에서 서비스 호출, 서비스에서 레퍼지토리를 호출
@RestController
@RequestMapping(value = "/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    //팔로우 추가
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @PostMapping("/{following_id}") //인자랑 이거 Path {} 안에 들어가는 거랑 똑같아야 함
    public BaseResponseDto<isFollowedResponseDto> follow(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long following_id) {
        return new BaseResponseDto<isFollowedResponseDto>(followService.follow(user, following_id));
    }

    // 팔로우 삭제
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @DeleteMapping("/{following_id}")
    public BaseResponseDto<isFollowedResponseDto> unFollow(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long following_id) {
        return new BaseResponseDto<isFollowedResponseDto>(followService.unFollow(user, following_id));
    }


    //팔로우 유무 체크
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @GetMapping("/isfollowed/{id}")
    public BaseResponseDto<isFollowedResponseDto> profile(@PathVariable Long id, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<isFollowedResponseDto>(followService.profile(id,user));
    }

    //내 팔로워 명단조회
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @GetMapping("/me/follower")
    public SliceResponseDto<meFollowersResponseDto> meFollowerList(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(followService.meFollowerList(user.getUserId(), pageable));
    }
    //내 팔로잉 명단조회
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @GetMapping("/me/following")
    public SliceResponseDto<meFollowingsResponseDto> meFollowingList(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(followService.meFollowingList(user.getUserId(), pageable));
    }

    //타인 프로필을 봤을 때 타인의 팔로워 명단조회
    @GetMapping("/{other_id}/follower")
    public SliceResponseDto<FollowersResponseDto> otherFollowerList(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long other_id, Pageable pageable){
        return new SliceResponseDto<>(followService.othersFollowerList(user.getUserId(), other_id, pageable));
    }
    //타인 프로필을 봤을 때 타인의 팔로잉 명단조회
    @GetMapping("/{other_id}/following")
    public SliceResponseDto<FollowingsResponseDto> otherFollowingList(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long other_id, Pageable pageable){
        return new SliceResponseDto<>(followService.othersFollowingList(user.getUserId(), other_id, pageable));
    }

    // 팔로잉 3명이하일 때, 팔로우 할 사람 추천
    @Secured({"ROLE_GENERAL", "ROLE_CURATOR"})
    @ApiOperation(value = "팔로우피드 - 팔로잉할 유저 추천", notes = "유저의 팔로잉이 3명이하일 경우, 팔로잉할 유저를 추천한다.")
    @GetMapping("/recommendation")
    public SliceResponseDto<FollowRecommendResponseDto> recommendation(@AuthenticationPrincipal ModugardenUser user, @PageableDefault(size=3) Pageable pageable){
        Slice<FollowRecommendResponseDto> responseDto = followService.recommendFollowingList(user.getUser(), pageable);
        return new SliceResponseDto<>(responseDto);
    }
}
