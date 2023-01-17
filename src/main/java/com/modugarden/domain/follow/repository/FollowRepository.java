package com.modugarden.domain.follow.repository;

import com.modugarden.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public class FollowRepository extends JpaRepository<Follow, Long> {
    int countByFollowerIdAndFollowingUserId(int id, String userId); // 팔로우 되어있는지 count하는 메서드

    @Modifying
    @Transactional
    void deleteByFollowingIdAndFollowerId(int id1, int id2); // 언팔로우 메서드
}
