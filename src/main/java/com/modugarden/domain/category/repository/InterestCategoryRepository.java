package com.modugarden.domain.category.repository;

import com.modugarden.domain.category.repository.entity.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {
    Optional<InterestCategory> findByCategory(String categoryName);
}
