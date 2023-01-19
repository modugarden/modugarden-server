package com.modugarden.domain.follow.controller;

import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import com.modugarden.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public @ResponseBody String follow(@AuthenticationPrincipal User user, @PathVariable int id) {
        // user가 아닌 dto를 써줘야 함
        // 원래는 UserService말고 객체가 와야 함
        //        User fromUser = userDetail.getUser();
        Optional<User> oToUser = userRepository.findById(id);
        User toUser = oToUser.get();

        //access를 private에서 public으로 바꿈
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollowingUser(toUser);

        followRepository.save(follow);

        return "ok";
    }

    @DeleteMapping("/follow/{following_id}")
    public @ResponseBody String unFollow(@AuthenticationPrincipal User user, @PathVariable int id) {
        // 원래는 UserService말고 객체가 와야 함
        // User fromUser = userDetail.getUser();
        Optional<User> oToUser = userRepository.findById(id);
        User toUser = oToUser.get();

        followRepository.deleteByFollowingIdAndFollowerId(user.getId(), user.getId());
//        List<Follow> follows = followRepository.findAll();
        return "ok";
    }

    public String profile(@PathVariable Long id, @AuthenticationPrincipal User User) {
        // user말고 원래는 User 객체가 와야 함
        User user = user.getId();
        int followcheck = followRepository.countByFollowerIdAndFollowingUserId(user.getId(), id);
    }
}
