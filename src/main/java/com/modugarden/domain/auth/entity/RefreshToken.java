package com.modugarden.domain.auth.entity;


import lombok.AccessLevel;
import lombok.Getter;
//import org.springframework.data.annotation.Id;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken{

    @Id
    @Column
    private String userEmail;

    @Column
    private String refreshToken;


    public RefreshToken(String userEmail, String refreshToken) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
    }
}

// @RedisHash의 value와 @Id를 붙인 필드의 값을 합쳐 Redis의 key로 사용
// 예) refreshToken 필드의 값이 1이면, Redis의 key = refreshToken:1