package com.backend.server.config;

import static com.backend.server.support.TestDatabaseUtil.PASSWORD;
import static com.backend.server.support.TestDatabaseUtil.URL_TEMPLATE;
import static com.backend.server.support.TestDatabaseUtil.USER;

import com.backend.server.support.TestDatabaseUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestDatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL_TEMPLATE + TestDatabaseUtil.createTestDatabase());
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(5);
        config.setPoolName("TestHikariPool");

        return new HikariDataSource(config);
    }
}
