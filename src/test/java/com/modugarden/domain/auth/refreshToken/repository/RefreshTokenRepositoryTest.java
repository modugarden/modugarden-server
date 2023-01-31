package com.modugarden.domain.auth.refreshToken.repository;

import com.modugarden.domain.auth.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired RefreshTokenRepository refreshTokenRepository;

    @Commit
    @Test
    void test(){
        RefreshToken refreshToken = new RefreshToken("refreshTokensample", "alsjung8@gmail.com");

        refreshTokenRepository.save(refreshToken);

        refreshTokenRepository.findById(refreshToken.getUserEmail());

        refreshTokenRepository.delete(refreshToken);

    }
}