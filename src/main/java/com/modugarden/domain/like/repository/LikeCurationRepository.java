package com.modugarden.domain.like.repository;

import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.like.entity.LikeCuration;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeCurationRepository extends JpaRepository<LikeCuration, Long> {
    Optional<LikeCuration> findByUserAndCuration(User user, Curation curation);

    Optional<LikeCuration> deleteAllByCuration_Id(Long id);

    Long deleteByUser(User user);
}