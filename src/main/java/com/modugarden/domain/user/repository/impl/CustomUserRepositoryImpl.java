package com.modugarden.domain.user.repository.impl;

import com.modugarden.domain.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.modugarden.domain.category.repository.entity.QInterestCategory.interestCategory;
import static com.modugarden.domain.category.repository.entity.QUserInterestCategory.userInterestCategory;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

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
