package com.backend.server.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestDatabaseUtil {

    public static final String URL_TEMPLATE = "jdbc:postgresql://localhost:5432/";
    public static final String USER = "cec";
    public static final String PASSWORD = "1234";

    private static final String URL = "jdbc:postgresql://localhost:5432/cec";
    private static final String PREFIX = "test_db_";

    private static final ConcurrentMap<String, String> dbNames = new ConcurrentHashMap<>();
    private static final Random random = new Random();
    private static final AtomicBoolean isTriggered = new AtomicBoolean(false);

    /**
     * db 를 새로 생성합니다.
     */
    public static String createTestDatabase() {
        final String dbId = String.valueOf(random.nextLong(0L, Long.MAX_VALUE));
        final String dbName = PREFIX + dbId;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE \"" + dbName + "\"");
            System.out.println(dbName + " 생성 성공.");
        } catch (SQLException e) {
            throw new RuntimeException("DB 생성 실패: " + dbName, e);
        }

        dbNames.put(dbId, dbName);
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
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement()) {
            for (String dbName : dbNames.values()) {
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
