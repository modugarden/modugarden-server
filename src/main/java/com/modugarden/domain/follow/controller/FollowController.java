package com.modugarden.domain.follow.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.status.enums.BaseResponseStatus;
import com.modugarden.domain.follow.dto.isFollowedResponseDto;
import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class FollowController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;

    @PostMapping("/follow/{following_id}")
    public @ResponseBody BaseResponseDto<isFollowedResponseDto> follow(@AuthenticationPrincipal User user, @PathVariable Long id) {
        // user가 아닌 dto를 써줘야 함
        // 원래는 UserService말고 객체가 와야 함
        // User fromUser = userDetail.getUser();
        Optional<User> oToUser = userRepository.findById(id);
        Long fromUser = user.getId();
        User toUser = oToUser.get();

        //access를 private에서 public으로 바꿈
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollowingUser(toUser);

        followRepository.save(follow);

        return new BaseResponseDto(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping("/follow/{following_id}")
    public @ResponseBody BaseResponseDto<isFollowedResponseDto> unFollow(@AuthenticationPrincipal User user, @PathVariable Long id) {
        // 원래는 UserService말고 객체가 와야 함
        // User fromUser = userDetail.getUser();
        Optional<User> oToUser = userRepository.findById(id);
        //User 대신 user 객체가 와야 함
        Long fromUser = user.getId();
        User toUser = oToUser.get();

        followRepository.deleteByFollowingIdAndFollowerId(user.getId(), oToUser.get().getId());
        //리턴을 dto로 해야 한다.
        return new BaseResponseDto<>(BaseResponseStatus.SUCCESS);
    }


    //팔로우 유무 체크
    public BaseResponseDto<isFollowedResponseDto> profile(@PathVariable Long id, @AuthenticationPrincipal User user) {

        Optional<User> oToUser = userRepository.findById(id);
        //User 대신 user 객체가 와야 함

        int followcheck = followRepository.countByFollowerIdAndFollowingUserId(user.getId(), oToUser.get().getId());
        if(followcheck==0){
            //팔로우 안함
            return new BaseResponseDto<>(new isFollowedResponseDto(false));
        }
        else{
            return new BaseResponseDto<>(new isFollowedResponseDto(true));
        }
    }
}
