package com.modugarden.domain.auth.entity;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@RedisHash(value = "refreshToken", timeToLive = 1209600000) // @RedisHash : Redis 스토리지에 저장할 객체 클래스에 설정, timeToLive 단위는 초, timeToLive 시간이 지나면 Redis에서 삭제됨
public class RefreshToken{

    @Id // javax.persistence.Id 대신 org.springframework.data.annotation.Id
    private String userEmail;

    private String refreshToken;


    public RefreshToken(String userEmail, String refreshToken) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
    }
}

// @RedisHash의 value와 @Id를 붙인 필드의 값을 합쳐 Redis의 key로 사용
// 예) refreshToken 필드의 값이 1이면, Redis의 key = refreshToken:1