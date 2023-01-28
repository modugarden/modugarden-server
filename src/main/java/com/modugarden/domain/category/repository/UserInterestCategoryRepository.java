package com.modugarden.domain.category.repository;


import com.modugarden.domain.category.entity.UserInterestCategory;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestCategoryRepository extends JpaRepository<UserInterestCategory, Long> {
    List<UserInterestCategory> findByUser(User user);

    void deleteAllByUser(User user);
}
