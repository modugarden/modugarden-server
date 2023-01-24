package com.modugarden.domain.follow.repository;

import com.modugarden.domain.follow.entity.Follow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    //팔로우 유무 체크
    @Query(value = "SELECT count(f) FROM Follow f WHERE f.followingUser.id =:fromUserId AND f.user.id =:toUserId") //jpql
    // 함수의 반환 값이 0이면 팔로우하는 사람이 없다. 1 이상이면 팔로우하는 사람이 있다.
    int countByUserAndFollowingUser(Long fromUserId, Long toUserId); // 팔로우 되어있는지 count하는 메서드

    //팔로워 삭제
    int deleteByFollowingUserAndUser(Long fromUserId, Long toUserId); // 언팔로우 메서드
    //userid가 int인가 long인가 -> long이다.

    //slice로 변경
    //반환형을 slice user로
    Slice<Follow> findByFollowingUser(Long fromUserId, Pageable pageable);

    Slice<Follow> findByUser(Long toUserId, Pageable pageable);
}
