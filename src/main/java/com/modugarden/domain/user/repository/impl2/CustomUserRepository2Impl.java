package com.modugarden.domain.user.repository.impl2;

import com.modugarden.domain.category.entity.QInterestCategory;
import com.modugarden.domain.category.entity.QUserInterestCategory;
import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.user.dto.UserInfoResponseDto;
import com.modugarden.domain.user.entity.QUser;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.CustomUserRepository2;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.modugarden.domain.category.entity.QInterestCategory.*;
import static com.modugarden.domain.category.entity.QUserInterestCategory.*;
import static com.modugarden.domain.user.entity.QUser.*;

@Repository // 필요한가???????????????
@RequiredArgsConstructor
public class CustomUserRepository2Impl implements CustomUserRepository2 {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> readUserInfo(Long userId) {
        User findUser = jpaQueryFactory
                .selectFrom(user)
                .where(user.id.eq(userId))
                .fetchOne();
        return Optional.ofNullable(findUser);
    }

    @Override
    public List<String> readUserInterestCategory(Long userId) {
        List<String> categories = jpaQueryFactory
                .select(interestCategory.category)
                .from(userInterestCategory)
                .join(userInterestCategory.category, interestCategory)
                .where(userInterestCategory.user.id.eq(userId))
                .fetch();
        return categories;
    }
}
