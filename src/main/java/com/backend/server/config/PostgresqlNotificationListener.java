package com.backend.server.config;

import com.backend.server.model.entity.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Profile("postgre")
@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresqlNotificationListener {

    private final DataSource dataSource;
    private final SseEmitterService sseEmitterService;
    private final ObjectMapper objectMapper;

    private Connection listenConnection;
    private ExecutorService executorService;

    @PostConstruct
    public void startListening() {
        executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try {
                listenConnection = dataSource.getConnection();
                PGConnection pgConnection = listenConnection.unwrap(PGConnection.class);

                try (Statement stmt = listenConnection.createStatement()) {
                    stmt.execute("LISTEN notifications");
                    log.info("리슨");
                }
                //스레드 살아있으면 계속돌림
                while (!Thread.currentThread().isInterrupted()) {
                    PGNotification[] notifications = pgConnection.getNotifications();
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            handleNotification(notification);
                        }
                    }
                    Thread.sleep(1000);   //10초마다 폴링
                }

            } catch (SQLException | InterruptedException e) {
                log.error("리슨 오류", e);
            } finally {
                cleanup();
            }
        });
    }

    private void handleNotification(PGNotification notification) {
        try {
            //String channel = notification.getName();
            String body = notification.getParameter();

            Notification notificationEntity = objectMapper.readValue(body, Notification.class);
            Long userId = notificationEntity.getUserId();

            // 사용자에게 알림 전송
            sseEmitterService.sendToClient(userId, notificationEntity);

            log.info("사용자 {}에게 알림 전송 완료", userId);

        } catch (Exception e) {
            log.error("알림 처리 실패 - 채널: {}", notification.getName(), e);
        }
    }


    @PreDestroy
    public void cleanup() {
        try {
            if (listenConnection != null && !listenConnection.isClosed()) {
                try (Statement statement = listenConnection.createStatement()) {
                    statement.execute("UNLISTEN notifications");
                }
                listenConnection.close();
                log.info("PostgreSQL LISTEN 종료됨");
            }
            if (executorService != null) {
                executorService.shutdownNow();
            }
        } catch (SQLException e) {
            log.error("PostgreSQL LISTEN 종료 중 오류", e);
        }
    }
}
