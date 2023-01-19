package com.modugarden.domain.follow.repository;

import com.modugarden.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    //팔로우 유무 체크
    @Query(value = "SELECT count(*) FROM follow WHERE fromUserId =:fromUserId AND toUserId =:toUserId")
    // 함수의 반환 값이 0이면 팔로우하는 사람이 없다. 1 이상이면 팔로우하는 사람이 있다.
    int countByFollowerIdAndFollowingUserId(Long fromUserId, Long toUserId); // 팔로우 되어있는지 count하는 메서드

    //팔로워 삭제
    @Modifying
    @Transactional
    int deleteByFollowingIdAndFollowerId(Long fromUserId, Long toUserId); // 언팔로우 메서드
    //userid가 int인가 long인가 -> long이다.
}
