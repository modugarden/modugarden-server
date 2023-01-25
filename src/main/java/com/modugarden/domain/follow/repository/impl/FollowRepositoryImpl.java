package com.modugarden.domain.follow.repository.impl;

import com.modugarden.domain.follow.repository.FollowRepository;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.modugarden.domain.follow.entity.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {
    private final JPAQueryFactory queryFactory;
    public boolean exists(Long userId, Long followingUserId) {
        Integer fetchOne = queryFactory.selectOne().from(follow).where((follow.user.id.eq(userId)).and(follow.followingUser.id.eq(followingUserId))).fetchFirst();
        return fetchOne != null;
    }
}
