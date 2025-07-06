package com.backend.server.model.repository.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Profile("postgre")
@Component
@RequiredArgsConstructor
@Slf4j
public class PostgresqlSendNotification implements SendNotification {
    private final DataSource dataSource;
    @Override
    public void sendNotification(String channel, String payload) {
        log.info("PG_NOTIFY 채널={}, payload={}", channel, payload);
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT pg_notify(?, ?)"; //select로 postgresql 내장 함수 실행
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, channel);
                statement.setString(2, payload);
                statement.execute();
            }
        }catch (SQLException e) {
            log.error("PostgreSQL NOTIFY 안감. - 채널: {}", channel, e);
        }
    }
}
