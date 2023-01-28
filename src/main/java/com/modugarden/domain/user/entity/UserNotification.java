package com.modugarden.domain.user.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public UserNotification(Boolean commentOnOff, Boolean followOnOff, Boolean serviceOnOff, Boolean marketingOnOff) {
        this.commentOnOff = commentOnOff;
        this.followOnOff = followOnOff;
        this.serviceOnOff = serviceOnOff;
        this.marketingOnOff = marketingOnOff;
    }

    public void updateNotification(Boolean commentOnOff, Boolean followOnOff, Boolean serviceOnOff, Boolean marketingOnOff) {
        this.commentOnOff = commentOnOff;
        this.followOnOff = followOnOff;
        this.serviceOnOff = serviceOnOff;
        this.marketingOnOff = marketingOnOff;
    }
}
