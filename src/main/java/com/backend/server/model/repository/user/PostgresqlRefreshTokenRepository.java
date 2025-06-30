package com.backend.server.model.repository.user;

import com.backend.server.model.entity.PostgresqlRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostgresqlRefreshTokenRepository extends JpaRepository<PostgresqlRefreshToken, Long> {
    Optional<PostgresqlRefreshToken> findByKey(String key);
    void deleteByKey(String key);
}
