package com.modugarden.domain.user.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean commentOnOff;

    @Column(nullable = false)
    private Boolean followOnOff;

    @Column(nullable = false)
    private Boolean serviceOnOff;

    @Column(nullable = false)
    private Boolean marketingOnOff;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
