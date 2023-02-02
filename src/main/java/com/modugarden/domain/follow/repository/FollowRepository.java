package com.modugarden.domain.follow.repository;

import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long>, CustomFollowRepository {
    //팔로워 삭제
    int deleteByUser_IdAndFollowingUser_Id(Long fromUserId, Long toUserId); // 언팔로우 메서드

    //팔로워 명단조회
    //내가 팔로잉 유저
    @Query(value = "select f.user from Follow f where f.followingUser.id=:userId")
    Slice<User> findByFollowingUser_Id(@Param("userId") Long userId, Pageable pageable);

    //팔로잉 명단조회
    @Query(value = "select f.followingUser from Follow f where f.user.id=:userId" )
    Slice<User> findByUser_Id(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select f.followingUser.id from Follow f where f.user.id=:userId" )
    List<Long> ffindByFollowingUser_Id(@Param("userId") Long userId, Pageable pageable);

    //유저아이디로 팔로워 수 알기
    Long countByFollowingUser_Id(Long user_id);
}
