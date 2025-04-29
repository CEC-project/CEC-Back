package com.backend.server.config;

import com.backend.server.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestMatcher requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/swagger-ui.html"),
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/swagger-resources/**"),
                new AntPathRequestMatcher("/swagger-resources"),
                new AntPathRequestMatcher("/webjars/**"),
                new AntPathRequestMatcher("/configuration/ui"),
                new AntPathRequestMatcher("/configuration/security"),
                new AntPathRequestMatcher("/api/auth/sign-in"),
                new AntPathRequestMatcher("/api/auth/token/refresh"),
                // 아래는 나중에 제거해야할 api 경로
                new AntPathRequestMatcher("/api/excel/import-users")
        );

        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(requestMatcher).permitAll()
                    // 관리자 경로
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    // 나머지 모든 요청은 인증 필요
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(configurer ->
                    configurer.accessDeniedHandler(
                            (req, res, ex) -> {
                                // @PreAuthorize 에서 권한이 없다고 예외가 발생시, RestControllerAdvise 로 넘김
                                throw ex;
                            })
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 