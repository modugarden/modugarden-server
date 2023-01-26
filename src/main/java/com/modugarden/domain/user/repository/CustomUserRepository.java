package com.modugarden.domain.user.repository;

import java.util.List;

public interface CustomUserRepository {

    List<String> readUserInterestCategory(Long userId);
}
