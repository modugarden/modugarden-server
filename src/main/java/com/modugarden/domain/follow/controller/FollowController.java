package com.modugarden.domain.follow.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.follow.dto.FollowRecommendResponseDto;
import com.modugarden.domain.follow.dto.FollowersResponseDto;
import com.modugarden.domain.follow.dto.FollowingsResponseDto;
import com.modugarden.domain.follow.dto.isFollowedResponseDto;
import com.modugarden.domain.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 컨트롤러에서 서비스 호출, 서비스에서 레퍼지토리를 호출
@RestController
@RequestMapping(value = "/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    //팔로우 추가
    @PostMapping("/{following_id}") //인자랑 이거 Path {} 안에 들어가는 거랑 똑같아야 함
    public BaseResponseDto<isFollowedResponseDto> follow(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long following_id) {
        return new BaseResponseDto<isFollowedResponseDto>(followService.follow(user, following_id));
    }

    // 팔로우 삭제
    @DeleteMapping("/{following_id}")
    public BaseResponseDto<isFollowedResponseDto> unFollow(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long following_id) {
        return new BaseResponseDto<isFollowedResponseDto>(followService.unFollow(user, following_id));
    }


    //팔로우 유무 체크
    @GetMapping("/isfollowed/{id}")
    public BaseResponseDto<isFollowedResponseDto> profile(@PathVariable Long id, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<isFollowedResponseDto>(followService.profile(id,user));
    }

    //내 팔로워 명단조회
    @GetMapping("/me/follower")
    public SliceResponseDto<FollowersResponseDto> meFollowerList(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(followService.meFollowerList(user.getUserId(), pageable));
    }
    //내 팔로잉 명단조회
    @GetMapping("/me/following")
    public SliceResponseDto<FollowingsResponseDto> meFollowingList( @AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
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

    @GetMapping("/recommendation")
    public BaseResponseDto<List<FollowRecommendResponseDto>> recommendation(@AuthenticationPrincipal ModugardenUser user, @PageableDefault(size=3) Pageable pageable){
        List<FollowRecommendResponseDto> responseDto = followService.recommendFollowingList(user.getUser(), pageable);
        return new BaseResponseDto<>(responseDto);
    }
}
