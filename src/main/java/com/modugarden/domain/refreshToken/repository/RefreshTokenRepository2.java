package com.modugarden.domain.refreshToken.repository;

import com.modugarden.domain.refreshToken.entity.RefreshToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository2 {
    private RedisTemplate redisTemplate;

    public RefreshTokenRepository2(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getUserEmail(), refreshToken.getRefreshToken());
        redisTemplate.expire(refreshToken.getUserEmail(), 60L, TimeUnit.SECONDS);
    }

    public Optional<RefreshToken> findById(final String userEmail) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(userEmail);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(userEmail, refreshToken));
    }

    public void delete(final RefreshToken refreshToken){
        redisTemplate.delete(refreshToken.getUserEmail());
    }
}
