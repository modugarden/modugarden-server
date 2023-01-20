package com.modugarden.domain.user.repository;

import com.modugarden.domain.user.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
}
