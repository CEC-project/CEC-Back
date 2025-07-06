package com.backend.server.model.repository.keyValue;

import java.time.Duration;

public interface KeyValueRepository {
    void set(String key, String value, Duration timeToLive);
    String get(String key);
    void delete(String key);
}
