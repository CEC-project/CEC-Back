package com.backend.server.config;

import com.backend.server.model.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    // 사용자별 Emitter 저장소
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 사용자별 하트비트 executor 저장소
    private final Map<Long, ScheduledExecutorService> executors = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(Long userId) {
        // SSE 연결 객체 생성. 10분 지나면 연결 끊음. 클라이언트가 연결되어도 계속 쏘는 데이터 누수 방지
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
        emitters.put(userId, emitter);

        // 하트비트용 executor 설정
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executors.put(userId, executor);

        executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().name("ping").data("♥"));
            } catch (IOException e) {
                cleanup(userId);
            }
        }, 0, 30, TimeUnit.SECONDS); // 30초마다 하트비트 전송

        emitter.onCompletion(() -> cleanup(userId));
        emitter.onTimeout(() -> cleanup(userId));

        return emitter;
    }

    public void sendToClient(Long userId, Notification notification) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
            } catch (IOException e) {
                cleanup(userId);
            }
        }
    }

    // 연결 및 스케줄러 정리
    private void cleanup(Long userId) {
        emitters.remove(userId);
        ScheduledExecutorService executor = executors.remove(userId);
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
