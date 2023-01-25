package com.modugarden.domain.user.repository.impl2;

import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.CustomUserRepository2;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.modugarden.domain.category.entity.QInterestCategory.*;
import static com.modugarden.domain.category.entity.QUserInterestCategory.*;
import static com.modugarden.domain.user.entity.QUser.*;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository2Impl implements CustomUserRepository2 {
    private final JPAQueryFactory jpaQueryFactory;
}
