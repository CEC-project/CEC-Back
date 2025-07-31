package com.backend.server.support;

import static com.backend.server.support.TestDatabaseUtil.TestDatabaseSettings.PASSWORD;
import static com.backend.server.support.TestDatabaseUtil.TestDatabaseSettings.URL;
import static com.backend.server.support.TestDatabaseUtil.TestDatabaseSettings.USER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class TestDatabaseUtil {

    @AllArgsConstructor
    @Getter
    public enum TestDatabaseSettings {
        URL("spring.datasource.url", "jdbc:postgresql://localhost:5432/cec"),
        URL_TEMPLATE("spring.datasource.url", "jdbc:postgresql://localhost:5432/"),
        DRIVER("spring.datasource.driver-class-name", "org.postgresql.Driver"),
        USER("spring.datasource.username", "cec"),
        PASSWORD("spring.datasource.password", "1234"),
        DB_NAME("test.db.name", "dbName");
        final String ymlPath, value;
    }

    public static final String PREFIX = "test_db_";

    private static final ConcurrentMap<String, String> dbNames = new ConcurrentHashMap<>();
    private static final Random random = new Random();
    private static final AtomicBoolean isTriggered = new AtomicBoolean(false);

    /**
     * db 를 새로 생성합니다.
     */
    public static String createTestDatabase() {
        final String dbId = String.valueOf(random.nextLong(0L, Long.MAX_VALUE));
        final String dbName = PREFIX + dbId;

        try (Connection conn = DriverManager.getConnection(URL.getValue(), USER.getValue(), PASSWORD.getValue());
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE \"" + dbName + "\"");
            System.out.println(dbName + " 생성 성공.");
        } catch (SQLException e) {
            throw new RuntimeException("DB 생성 실패: " + dbName, e);
        }

        dbNames.put(dbName, "true");
        if (!isTriggered.get()) {
            isTriggered.set(true);
            Runtime.getRuntime().addShutdownHook(new Thread(TestDatabaseUtil::dropAllTestDatabases));
        }
        return dbName;
    }

    /**
     * 아직 삭제되지 않은 db 들을 전부 삭제합니다.
     */
    public static void dropAllTestDatabases() {
        try (Connection conn = DriverManager.getConnection(URL.getValue(), USER.getValue(), PASSWORD.getValue());
                Statement stmt = conn.createStatement()) {
            for (String dbName : dbNames.keySet()) {
                try {
                    stmt.executeUpdate("DROP DATABASE IF EXISTS \"" + dbName + "\" WITH ( FORCE )");
                    System.out.println(dbName + " 삭제 성공.");
                } catch (SQLException e) {
                    System.out.println(dbName + " 삭제시 에러 발생 : " + e.getMessage());
                }
            }
            dbNames.clear();
        } catch (SQLException e) {
            throw new RuntimeException("DB 삭제시 연결 실패: ", e);
        }
    }
}
