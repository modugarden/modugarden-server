package com.modugarden.domain.refreshToken.repository;

import com.modugarden.domain.refreshToken.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
