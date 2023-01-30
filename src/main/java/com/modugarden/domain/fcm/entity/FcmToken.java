package com.modugarden.domain.fcm.entity;

import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fcmToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FcmToken(String fcmToken, User user) {
        this.fcmToken = fcmToken;
        this.user = user;
    }

    public void update(String fcmToken){
        this.fcmToken = fcmToken;
    }
}
