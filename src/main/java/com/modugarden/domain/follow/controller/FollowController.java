package com.modugarden.domain.follow.controller;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.follow.dto.FollowResponseDto;
import com.modugarden.domain.follow.dto.isFollowedResponseDto;
import com.modugarden.domain.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 컨트롤러에서 서비스 호출, 서비스에서 레퍼지토리를 호출
@RestController
@RequestMapping(value = "/folllow")
public class FollowController {
    @Autowired
    private FollowService followService;

    //팔로우 추가
    @PostMapping("/{following_id}") //인자랑 이거 Path {} 안에 들어가는 거랑 똑같아야 함
    public BaseResponseDto<isFollowedResponseDto> follow(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long following_id) {
        return new BaseResponseDto(new BaseResponseDto<>(ErrorMessage.SUCCESS));
    }

    // 팔로우 삭제
    @DeleteMapping("/{following_id}")
    public BaseResponseDto<isFollowedResponseDto> unFollow(@AuthenticationPrincipal ModugardenUser user, @PathVariable Long following_id) {
        return new BaseResponseDto(new BaseResponseDto<>(ErrorMessage.SUCCESS));
    }


    //팔로우 유무 체크
    @GetMapping("/isfollowed/{id}")  //pathvariable 쓰면 {~~} 이거 써야 함
    public BaseResponseDto<isFollowedResponseDto> profile(@PathVariable Long id, @AuthenticationPrincipal ModugardenUser user) {
        //if으로 return이 두 개일 경우 밑과 같이 받아옴o
        int followcheck = followService.profile(id, user);
        if (followcheck == 0) {
            //팔로우 안함
            return new BaseResponseDto<>(new isFollowedResponseDto(false));
        } else {
            return new BaseResponseDto<>(new isFollowedResponseDto(true));
        }
    }

    //user가 following user을 following 함
    //following user을 user가 follower 함

    @GetMapping("/follower/{id}")
    public SliceResponseDto<FollowResponseDto> followerList(@PathVariable Long id, @AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(followService.followerList(id, user, pageable));
        //리턴을 리스트로
    }

    @GetMapping("/following/{id}")
    public SliceResponseDto<FollowResponseDto> followingList(@PathVariable Long id, @AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(followService.followingList(id, user, pageable));
        // 리턴을 팔로우 리스트를 해줘야 함
    }
}
