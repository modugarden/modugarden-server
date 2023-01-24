package com.modugarden.domain.user.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.user.entity.enums.UserAuthority;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAuthority authority;

    @Column(columnDefinition = "TEXT")
    private String profileImg;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_notification_id", nullable = false)
    private UserNotification notification;

    @Builder
    public User(String email, String password, String nickname,
                 String birth, UserAuthority authority, String profileImg,
                 UserNotification notification) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birth = birth;
        this.authority = authority;
        this.profileImg = profileImg;
        this.notification = notification;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImg) {
        this.profileImg = profileImg;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }
}
