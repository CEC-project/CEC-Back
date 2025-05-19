package com.backend.server.config;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.backend.server.model.entity.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SseEmitterService sseEmitterService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            Notification notification = objectMapper.readValue(body, Notification.class);
            Long userId = extractUserIdFromChannel(channel);

            sseEmitterService.sendToClient(userId, notification);

        } catch (Exception e) {
            log.error("Redis 구독 중 에러 발생", e);
        }
    }

    private Long extractUserIdFromChannel(String channel) {
        return Long.parseLong(channel.split(":")[1]);
    }
}
