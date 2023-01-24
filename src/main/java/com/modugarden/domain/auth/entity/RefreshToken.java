package com.modugarden.domain.auth.entity;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

// 우선 refreshToken RDBMS로 저장
@Entity
public class RefreshToken {
    @Id
    private String key;

    @Column(nullable = false)
    private String value;

    public void updateValue(String token) {
        this.value = token;
    }

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
