package com.modugarden.domain.user.repository.impl;

import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


import static com.modugarden.domain.category.repository.entity.QInterestCategory.interestCategory;
import static com.modugarden.domain.category.repository.entity.QUserInterestCategory.userInterestCategory;
import static com.modugarden.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> readUserInfo(Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(user)
                .where(user.id.eq(userId))
                .fetchOne());
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
