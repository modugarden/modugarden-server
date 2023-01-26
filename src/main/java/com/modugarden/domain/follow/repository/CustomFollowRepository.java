package com.modugarden.domain.follow.repository;

public interface CustomFollowRepository {
    boolean exists(Long userId, Long followingUserId);
}
