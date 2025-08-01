package com.backend.server.model.repository.keyValue;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Profile("!postgre")
@Repository
public class RedisKeyValueRepository implements KeyValueRepository {
    private final RedisTemplate<String,String> redisTemplate;
    @Override
    public void set(String key, String value, Duration timeToLive) {
        redisTemplate.opsForValue().set(key, value, timeToLive);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
