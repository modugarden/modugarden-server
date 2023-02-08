package com.modugarden.domain.follow.repository.impl;

import com.modugarden.domain.follow.entity.QFollow;
import com.modugarden.domain.follow.repository.CustomFollowRepository;
import com.modugarden.domain.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.modugarden.domain.category.entity.QInterestCategory.interestCategory;
import static com.modugarden.domain.category.entity.QUserInterestCategory.userInterestCategory;
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

    // 팔로우 추천할 id - 3명이하를 팔로잉하고 있을 때
    // 로그인 유저와 같은 카테고리, 팔로워 많은 순
    @Override
    public Slice<Long> recommend3FollowingId(User loginUser, Pageable pageable){
        QFollow follow2 = new QFollow("follow2");
        NumberPath<Long> categoryCount = Expressions.numberPath(Long.class, "categoryCount");
        NumberPath<Long> followingCount = Expressions.numberPath(Long.class, "followingCount");

        // 로그인한 유저의 카테고리 id들 조회
        List<Long> loginUserCategoryList = queryFactory
                .select(interestCategory.id)
                .from(userInterestCategory)
                .join(userInterestCategory.category, interestCategory)
                .where(userInterestCategory.user.id.eq(loginUser.getId()))
                .fetch();

        // 유저가 팔로잉하고 있는 유저 id들 조회
        List<Long> loginUserFollowingList = queryFactory.select(follow.followingUser.id).distinct()
                .from(follow)
                .where(follow.user.id.eq(loginUser.getId()))
                .fetch();

        // 유저와 같은 카테고리, 팔로워 많은 순
        List<Tuple> recommendTuple = queryFactory
                .select(follow.user.id,
                        ExpressionUtils.as(JPAExpressions.select(userInterestCategory.count())
                                .from(userInterestCategory)
                                .where(userInterestCategory.user.id.eq(follow.user.id).and(userInterestCategory.category.id.in(loginUserCategoryList))), "categoryCount"),
                        ExpressionUtils.as(JPAExpressions.select(follow2.count())
                                .from(follow2)
                                .where(follow2.followingUser.id.eq(follow.user.id)), "followingCount")
                )
                .from(follow)
                .groupBy(follow.user.id)
                .having(follow.user.id.notIn(loginUserFollowingList).and(follow.user.id.ne(loginUser.getId())))// 유저는 제외, 유저가 팔로잉하고 있는 사람은 제외
                .orderBy(categoryCount.desc(), followingCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .fetch();


        List<Long> recommendUserIds = recommendTuple.stream().map(tuple -> tuple.get(follow.user.id)).collect(Collectors.toList());

        Boolean hasNext = false;
        if (recommendUserIds.size() > pageable.getPageSize()) {
            recommendUserIds.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(recommendUserIds, pageable, hasNext);
    }

}
