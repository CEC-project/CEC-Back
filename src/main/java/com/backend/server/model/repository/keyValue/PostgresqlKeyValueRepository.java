package com.backend.server.model.repository.keyValue;

import com.backend.server.model.entity.PostgresqlRefreshToken;
import com.backend.server.model.repository.user.PostgresqlRefreshTokenRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("postgre")
@Repository
@RequiredArgsConstructor
public class PostgresqlKeyValueRepository implements KeyValueRepository {
    private final PostgresqlRefreshTokenRepository refreshTokenRepository;
    @Override
    public void set(String key, String value, Duration timeToLive) {
        long expiresAt = System.currentTimeMillis() + timeToLive.toMillis();
        PostgresqlRefreshToken refreshToken = refreshTokenRepository.findByKey(key).orElse(new PostgresqlRefreshToken());
        refreshToken.setKey(key);
        refreshToken.setValue(value);
        refreshToken.setExpiresAt(expiresAt);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public String get(String key) {
        return refreshTokenRepository.findByKey(key)
                .filter(e -> e.getExpiresAt() == null || e.getExpiresAt() > System.currentTimeMillis())
                .map(PostgresqlRefreshToken::getValue)
                .orElse(null);
    }

    @Override
    public void delete(String key) {
        refreshTokenRepository.deleteByKey(key);
    }
}
