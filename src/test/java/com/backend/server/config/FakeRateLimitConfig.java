package com.backend.server.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.backend.server.model.repository.rateLimit.RateLimitRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * rate limit 제한을 회피하기 위한 설정입니다.
 */
@TestConfiguration
public class FakeRateLimitConfig {

    @Bean
    @Primary
    public RateLimitRepository rateLimitRepository() {
        RateLimitRepository fakeRepository = Mockito.mock(RateLimitRepository.class);

        given(fakeRepository.add(anyString(), any())).willReturn(1L);
        given(fakeRepository.delete(anyString())).willReturn(1L);

        return fakeRepository;
    }

}
