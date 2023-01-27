package com.modugarden.domain.follow.repository;

import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long>, CustomFollowRepository{
    //팔로워 삭제
    int deleteByUser_IdAndFollowingUser_Id(Long fromUserId, Long toUserId); // 언팔로우 메서드
    //팔로잉 하고 있는 정보가 존재하는지 여부 체크
    Slice<Follow> findByFollowingUser(Long fromUserId, Pageable pageable);

    Slice<Follow> findByUser(Long toUserId, Pageable pageable);
    //유저아이디로 팔로워 수 알기
    Long countByUser_Id(Long user_id);
}
