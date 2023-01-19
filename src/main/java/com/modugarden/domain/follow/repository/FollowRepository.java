package com.modugarden.domain.follow.repository;

import com.modugarden.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    //팔로우 유무
    @Query(value = "SELECT count(*) FROM follow WHERE fromUserId =?1 AND toUserID =?2")
    int countByFollowerIdAndFollowingUserId(Long fromUserId, Long toUserId); // 팔로우 되어있는지 count하는 메서드

//    @Modifying
    @Transactional
    int deleteByFollowingIdAndFollowerId(Long fromUserId, Long toUserId); // 언팔로우 메서드
    //userid가 int인가 long인가 -> long이다.
}
