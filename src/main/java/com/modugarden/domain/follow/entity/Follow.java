package com.modugarden.domain.follow.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User followingUser;

    @Transient
    //엔티티 객체의 데이터와 테이블의 칼럼과 매핑하고 있는 관계를 제외하기 위해서 사용한다.
    private boolean matpal;

    @Transient
    private boolean principalMatpal;

    public Follow(User user, User followingUser) {
        this.user = user;
        this.followingUser = followingUser;
    }
}
