package com.modugarden.domain.follow.service;

import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

public class FollowService {
    @Autowired
    FollowRepository followRepository;
    @Autowired
    FollowService followService;

    public void save(int login_id, int page_id) { // 팔로우

        User follow = followService.findById(login_id);
        User following =  followService.findById(page_id);

        Follow f = new Follow(follow, following);
        followRepository.save(f);
    }
    public void deleteByFollowingIdAndFollowerId(int id1, int id2) { // 언팔로우
        followRepository.deleteByFollowingIdAndFollowerId(id2, id1);
    }

    public boolean find(int id, String userId) { // 팔로우가 되어있는지를 확인하기위해
        if(followRepository.countByFollowerIdAndFollowingUserId(id, userId) == 0)
            return false; // 팔로우 안되어있음
        return true; // 되어있음
    }
}
