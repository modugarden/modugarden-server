package com.modugarden.domain.user.repository;

import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}

