package com.modugarden.domain.block.repository;

import com.modugarden.domain.block.entity.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<UserBlock, Long> {
}
