package com.modugarden.domain.follow.repository;

import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomFollowRepository {
    boolean exists(Long userId, Long followingUserId);
    public Slice<Long> recommend3FollowingId(User user, Pageable pageable);
}
