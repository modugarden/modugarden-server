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

    public boolean exists(Long userId, Long followingUserId) {
        Integer fetchOne = queryFactory.selectOne().from(follow).where((follow.user.id.eq(userId)).and(follow.followingUser.id.eq(followingUserId))).fetchFirst();
        return fetchOne != null;
    }

    // 팔로우 추천할 id
    // 로그인 유저와 같은 카테고리, 팔로워 많은 순
    @Override
    public List<Long> recommend3FollowingId(User user, long offset, int size){
        QFollow follow2 = new QFollow("follow2");
        NumberPath<Long> categoryCount = Expressions.numberPath(Long.class, "categoryCount");
        NumberPath<Long> followingCount = Expressions.numberPath(Long.class, "followingCount");

        // 로그인한 유저의 카테고리 id들 조회
        List<Long> loginUserCategoryList = queryFactory
                .select(interestCategory.id)
                .from(userInterestCategory)
                .join(userInterestCategory.category, interestCategory)
                .where(userInterestCategory.user.id.eq(user.getId()))
                .fetch();


        // 유저가 팔로잉하고 있는 유저 id들 조회
        List<Long> loginUserFollowingList = queryFactory.select(follow.user.id).distinct()
                .from(follow)
                .where(follow.followingUser.id.eq(user.getId()))
                .fetch();


        // 유저와 같은 카테고리, 팔로워 많은 순
        List<Tuple> recommendTuple = queryFactory
                .select(follow.user.id,
                        ExpressionUtils.as(JPAExpressions.select(userInterestCategory.count())
                                .from(userInterestCategory)
                                .where(userInterestCategory.user.id.eq(follow.user.id).and(userInterestCategory.category.id.in(loginUserCategoryList))), "categoryCount"),
                        ExpressionUtils.as(JPAExpressions.select(follow.count())
                                .from(follow2)
                                .where(follow2.user.id.eq(follow.user.id)), "followingCount")
                )
                .from(follow)
                .where(follow.user.id.ne(user.getId()).and(follow.followingUser.id.ne(user.getId())))// 유저는 제외, 유저가 팔로잉하고 있는 사람은 제외
                .groupBy(follow.user.id)
                .having(follow.user.id.notIn(loginUserFollowingList))
                .orderBy(categoryCount.desc(), followingCount.desc())
                .offset(offset)
                .limit(size)
                .fetch();


        List<Long> recommendUserIds = recommendTuple.stream().map(tuple -> tuple.get(follow.user.id)).collect(Collectors.toList());

        return recommendUserIds;

    }

}
