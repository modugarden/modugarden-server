package com.modugarden.domain.fcm.repository;

import com.modugarden.domain.fcm.entity.FcmToken;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmRepository extends JpaRepository<FcmToken, Long> {

    List<FcmToken> findByUser(User user);
    Optional<FcmToken> findByFcmToken(String fcmToken);

    Optional<FcmToken> deleteByFcmToken(String fcmToken);

    Long deleteByUser(User user);
}
