package com.backend.server.config;

import com.backend.server.util.YamlUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

// 이 클래스를 상속하고, 아래 어노테이션을 붙인 클래스에서는 DB 테스트가 가능합니다.
// 클래스별로 새로운 db 를 만듭니다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractPostgresConfigure {

    private static final Map<String, Map<DbSettings, String>> classScopedDbSettings = new ConcurrentHashMap<>();

    enum DbSettings {
        OLD_DB_URL, NEW_DB_URL, DB_NAME, USER, PASSWORD
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        // 0. 동일 클래스에서 db 를 설정한적 있는지 확인합니다.
        // 1. 기존 db 설정을 읽어옵니다.
        // 2. 새로운 테스트용 db 를 만들고 연결합니다.
        // 3. 테스트가 끝나고 테스트용 db 를 제거할수 있도록, Map에 관련 설정을 저장합니다.

        // 0. 동일 클래스에서 db 를 설정한적 있는지 확인합니다.
        String testClassName = getClassName();
        if (classScopedDbSettings.containsKey(getClassName())) {
            Map<DbSettings, String> dbSettings = classScopedDbSettings.get(testClassName);
            registry.add("spring.datasource.url", () -> dbSettings.get(DbSettings.NEW_DB_URL));
            return;
        }

        // 1. 기존 db 설정을 읽어옵니다.
        String oldUrl = YamlUtil.getNested("spring", "datasource", "url")
                .orElse("jdbc:postgresql://localhost:5432/").toString();
        String user = YamlUtil.getNested("spring", "datasource", "username")
                .orElse("cec").toString();
        String password = YamlUtil.getNested("spring", "datasource", "password")
                .orElse("1234").toString();

        // 2. 새로운 테스트용 db 를 만들고 연결합니다.
        String dbName = "test_" + UUID.randomUUID().toString().substring(0, 8);
        String baseUrl = StringUtils.substringBeforeLast(oldUrl, "/");
        String newUrl = baseUrl + "/" + dbName;

        String createDb = "CREATE DATABASE " + dbName;
        System.out.println("테스트용 DB 생성 시도 : " + createDb);
        try (Connection conn = DriverManager.getConnection(oldUrl, user, password);
                Statement stmt = conn.createStatement()) {
            stmt.execute(createDb);
        } catch (SQLException e) {
            throw new RuntimeException("DB 생성 실패 : " + createDb, e);
        }

        registry.add("spring.datasource.url", () -> newUrl);

        // 3. 테스트가 끝나고 테스트용 db 를 제거할수 있도록, Map에 관련 설정을 저장합니다.
        Map<DbSettings, String> dbSettings = new HashMap<>();
        dbSettings.put(DbSettings.USER, user);
        dbSettings.put(DbSettings.PASSWORD, password);
        dbSettings.put(DbSettings.OLD_DB_URL, oldUrl);
        dbSettings.put(DbSettings.NEW_DB_URL, newUrl);
        dbSettings.put(DbSettings.DB_NAME, dbName);
        classScopedDbSettings.put(testClassName, dbSettings);
    }

    @AfterAll
    static void cleanUp() {
        Map<DbSettings, String> dbSettings = classScopedDbSettings.get(getClassName());
        String url = dbSettings.get(DbSettings.OLD_DB_URL);
        String name = dbSettings.get(DbSettings.DB_NAME);
        String user = dbSettings.get(DbSettings.USER);
        String password = dbSettings.get(DbSettings.PASSWORD);

        // 1. 연결된 세션 종료 SQL 문
        String terminateSessions = "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = ?";
        // 2. 테스트용 DB 삭제 SQL 문
        String dropDb = "DROP DATABASE IF EXISTS " + name;

        System.out.println("테스트용 DB 삭제 시도 : " + dropDb);
        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement ps = conn.prepareStatement(terminateSessions);
                Statement stmt = conn.createStatement()
        ) {
            ps.setString(1, name);
            ps.execute();
            stmt.execute(dropDb);
        } catch (SQLException e) {
            throw new RuntimeException("DB 삭제 실패 : " + dropDb, e);
        }
    }

    private static String getClassName() {
        // 현재 테스트 클래스의 이름을 얻음
        // 스택 트레이스를 이용한 편법
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .map(StackTraceElement::getClassName)
                .filter(name -> name.endsWith("Test") || name.endsWith("Tests")) // 원하는 네이밍 규칙 반영
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 클래스 이름을 찾을 수 없음"));
    }
}