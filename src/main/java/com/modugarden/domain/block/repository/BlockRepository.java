package com.modugarden.domain.block.repository;

import com.modugarden.domain.block.entity.UserBlock;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<UserBlock, Long> {

    Optional<UserBlock> findByUserAndBlockUser(User user, User blockUser);
}
