package com.modugarden.domain.block.repository;

import com.modugarden.domain.block.entity.UserBlock;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<UserBlock, Long> {

    Optional<UserBlock> findByUserAndBlockUser(User user, User blockUser);
    Optional<UserBlock> deleteByUser_IdAndBlockUser_Id(Long userId, Long blockUserId);

    Slice<UserBlock> findByUser_Id(Long id, Pageable pageable);
}
