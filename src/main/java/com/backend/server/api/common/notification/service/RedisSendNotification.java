package com.backend.server.api.common.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Profile("!postgre")
@Component
@RequiredArgsConstructor
public class RedisSendNotification implements SendNotification{
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public void sendNotification(String channel, String payload) {
        redisTemplate.convertAndSend(channel,payload);
    }
}
