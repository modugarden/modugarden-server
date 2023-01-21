package com.modugarden.domain.user.repository;

import com.modugarden.domain.user.dto.UserInfoResponseDto;
import com.modugarden.domain.user.entity.User;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository2 {

    Optional<User> readUserInfo(Long userId);
    List<String> readUserInterestCategory(Long userId);
}
