package com.backend.server.config;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisScriptConfig {

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisScript<Long> rateLimit_add = RedisScript.of(
            new ClassPathResource("META-INF/scripts/rate_limit/add.lua"),
            Long.class);

    private final RedisScript<Long> rateLimit_delete = RedisScript.of(
            new ClassPathResource("META-INF/scripts/rate_limit/delete.lua"),
            Long.class);

    public Long rateLimitAdd(String key, long windowMillis, int threshold) {
        return redisTemplate.execute(
                rateLimit_add,
                Collections.singletonList(key),
                String.valueOf(windowMillis),
                String.valueOf(threshold));
    }

    public Long rateLimitDelete(String key) {
        return redisTemplate.execute(
                rateLimit_delete,
                Collections.singletonList(key));
    }
}
