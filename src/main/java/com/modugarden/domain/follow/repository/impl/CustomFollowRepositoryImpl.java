package com.modugarden.domain.follow.repository.impl;

import com.modugarden.domain.follow.repository.CustomFollowRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.modugarden.domain.follow.entity.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class CustomFollowRepositoryImpl implements CustomFollowRepository {
    private final JPAQueryFactory queryFactory;
    //내가 이 사람을 팔로우하고 있는지 안하고 있는지
    public boolean exists(Long userId, Long followingUserId) {
        Integer fetchOne = queryFactory.selectOne().from(follow).where((follow.user.id.eq(userId)).and(follow.followingUser.id.eq(followingUserId))).fetchFirst();
        return fetchOne != null;
    }
}
