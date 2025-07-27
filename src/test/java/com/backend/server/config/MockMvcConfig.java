package com.backend.server.config;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * MockMvc 로 spring security 커스텀 인증 필터까지 테스트하기 위한 커스텀 설정입니다.
 */
@TestConfiguration
public class MockMvcConfig {
    @Bean
    @Primary
    public MockMvc customMockMvc(WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();
    }
}
