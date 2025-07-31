package com.backend.server.config;

import static com.backend.server.support.TestDatabaseUtil.TestDatabaseSettings.URL_TEMPLATE;

import com.backend.server.support.TestDatabaseUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;

/**
 * 테스트 격리를 위한 db 를 생성 / 삭제합니다.
 */
@TestConfiguration
public class TestDatabaseConfig {

    @Bean
    DynamicPropertyRegistrar apiPropertiesRegistrar() {
        return registry -> {
            registry.add(URL_TEMPLATE.getYmlPath(),
                    () -> URL_TEMPLATE.getValue() + TestDatabaseUtil.createTestDatabase());
            registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        };
    }
}
