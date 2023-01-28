package com.modugarden.domain.follow.repository;

import com.modugarden.domain.user.entity.User;

import java.util.List;

public interface CustomFollowRepository {
    boolean exists(Long userId, Long followingUserId);

    public List<Long> recommend3FollowingId(User user, long offset, int size);
}
