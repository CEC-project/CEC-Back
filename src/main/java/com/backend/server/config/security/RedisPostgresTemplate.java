package com.backend.server.config.security;

import java.time.Duration;

public interface RedisPostgresTemplate {
    void set(String key, String value, Duration timeToLive);
    String get(String key);
    void delete(String key);
}
