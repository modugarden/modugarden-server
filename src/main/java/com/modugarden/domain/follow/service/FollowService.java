package com.modugarden.domain.follow.service;

import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FollowService {
    @Autowired
    FollowRepository followRepository;
    @Autowired
    FollowService followService;

    public List<Follow> findByFollowingId(Long id) {
        return followRepository.findByToUserId(id);
    }
}