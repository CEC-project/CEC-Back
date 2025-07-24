package com.backend.server.model.repository.rateLimit;

import com.backend.server.api.common.exceptionHandler.exception.TooManyRequestException;
import com.backend.server.config.RedisScriptConfig;
import com.backend.server.config.annotation.rateLimit.LimitRequestPerTime;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRateLimitRepository implements RateLimitRepository {

    private final RedisScriptConfig scripts;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long add(String key, LimitRequestPerTime limit) throws TooManyRequestException {
        final long windowMillis = limit.timeUnit().toMillis(limit.time());
        final int threshold = limit.count();

        Long result = redisTemplate.execute(
                scripts.getRateLimit_add(),
                Collections.singletonList(key),
                String.valueOf(windowMillis),
                String.valueOf(threshold));

        // 요청 횟수 제한을 초과했을때
        if (result < 0) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime retryAvailableAt = Instant
                    .ofEpochMilli(-result + windowMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            Duration retryAfter = Duration.between(now, retryAvailableAt);
            throw new TooManyRequestException(limit, retryAfter);
        }

        return result;
    }

    @Override
    public Long delete(String key) {
        return redisTemplate.execute(
                scripts.getRateLimit_delete(),
                Collections.singletonList(key));
    }
}
