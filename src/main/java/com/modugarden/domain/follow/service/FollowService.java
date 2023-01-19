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

    public void deleteByFollowingIdAndFollowerId(Long id1, Long id2) { // 언팔로우
        followRepository.deleteByFollowingIdAndFollowerId(id2, id1);
    }
}
