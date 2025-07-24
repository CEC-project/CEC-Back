package com.backend.server.config;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RedisScriptConfig {

    private final RedisScript<Long> rateLimit_add = RedisScript.of(
            new ClassPathResource("META-INF/scripts/rate_limit/add.lua"),
            Long.class);
    private final RedisScript<Long> rateLimit_delete = RedisScript.of(
            new ClassPathResource("META-INF/scripts/rate_limit/delete.lua"),
            Long.class);
}
