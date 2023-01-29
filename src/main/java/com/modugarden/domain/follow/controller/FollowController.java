package com.modugarden.domain.follow.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.follow.dto.*;
import com.modugarden.domain.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping("/isfollowed/{id}")  //pathvariable 쓰면 {~~} 이거 써야 함
    public BaseResponseDto<isFollowedResponseDto> profile(@PathVariable Long id, @AuthenticationPrincipal ModugardenUser user) {
        //if으로 return이 두 개일 경우 밑과 같이 받아옴
        return new BaseResponseDto<isFollowedResponseDto>(followService.profile(id,user));
    }
    //user가 following user을 following 함
    //following user을 user가 follower 함

    //팔로워 명단조회
    @GetMapping("/me/follower")
    public SliceResponseDto<FollowersResponseDto> meFollowerList(@AuthenticationPrincipal @Valid ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(followService.meFollowerList(user.getUserId(), pageable));
    }
    //팔로잉 명단조회
    @GetMapping("/me/following")
    public SliceResponseDto<FollowingsResponseDto> meFollowingList( @AuthenticationPrincipal @Valid ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(followService.meFollowingList(user.getUserId(), pageable));
    }
    //타인 프로필을 봤을 때 타인의 팔로워 명단조회
    @GetMapping("/{user_id}/follower")
    public SliceResponseDto<FollowersResponseDto> otherFollowerList(@AuthenticationPrincipal @Valid ModugardenUser user, @PathVariable Long userId, Pageable pageable){
        return new SliceResponseDto<>(followService.othersFollowerList(userId,user.getUserId(),pageable));
    }
    //타인 프로필을 봤을 때 타인의 팔로잉 명단조회
    @GetMapping("/{user_id}/following")
    public SliceResponseDto<FollowingsResponseDto> otherFollowingList(@AuthenticationPrincipal @Valid ModugardenUser user, @PathVariable Long userId, Pageable pageable){
        return new SliceResponseDto<>(followService.othersFollowingList(userId,user.getUserId(),pageable));
    }

    @GetMapping("/recommendation")
    public BaseResponseDto<List<FollowRecommendResponseDto>> recommendation(@AuthenticationPrincipal ModugardenUser user, @PageableDefault(size=3) Pageable pageable){
        List<FollowRecommendResponseDto> responseDto = followService.recommendFollowingList(user.getUser(), pageable);
        return new BaseResponseDto<>(responseDto);
    }
}
