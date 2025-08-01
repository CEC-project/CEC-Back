package com.backend.server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestDatabaseConfig {

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        POSTGRES_CONTAINER.start();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(POSTGRES_CONTAINER.getJdbcUrl());
        config.setUsername(POSTGRES_CONTAINER.getUsername());
        config.setPassword(POSTGRES_CONTAINER.getPassword());
        config.setDriverClassName(POSTGRES_CONTAINER.getDriverClassName());
        config.setMaximumPoolSize(5);
        config.setPoolName("TestContainerHikariPool");

        return new HikariDataSource(config);
    }
}
