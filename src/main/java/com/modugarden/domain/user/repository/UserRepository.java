package com.modugarden.domain.user.repository;

import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    public Slice<User> findByNicknameLike(String nickname, Pageable pageable);

}

