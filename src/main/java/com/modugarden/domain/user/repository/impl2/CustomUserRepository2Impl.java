package com.modugarden.domain.user.repository.impl2;

import com.modugarden.domain.user.repository.CustomUserRepository2;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository2Impl implements CustomUserRepository2 {
    private final JPAQueryFactory jpaQueryFactory;
}
