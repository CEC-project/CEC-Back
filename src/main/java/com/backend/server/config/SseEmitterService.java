package com.backend.server.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.config.security.AccessTokenValidator;
import com.backend.server.config.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.backend.server.model.entity.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final AccessTokenValidator accessTokenValidator;

    public SseEmitter createEmitter(LoginUser loginUser, String token) {
        if (!accessTokenValidator.validateAccessToken(token)) {
            throw new BadCredentialsException("유효하지 않은 액세스 토큰입니다.");
        }

        Long tokenUserId = accessTokenValidator.getUserIdByAccessToken(token);
        if (!loginUser.getId().equals(tokenUserId)) {
            throw new IllegalArgumentException("토큰의 사용자 정보가 일치하지 않습니다.");
        }

        SseEmitter emitter = new SseEmitter(60 * 1000L); // 1분 타임아웃
        emitters.put(loginUser.getId(), emitter);

        emitter.onCompletion(() -> emitters.remove(tokenUserId));
        emitter.onTimeout(() -> emitters.remove(tokenUserId));

        return emitter;
    }

    public void sendToClient(Long userId, Notification notification) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}
