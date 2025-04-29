package com.backend.server.config;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.config.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*")); // 모든 Origin 허용
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(false); // "*"을 사용할 때는 반드시 false
                return config;
            }))
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
                                    (req, res, ex) ->
                                            responseMsg(res, "권한이 부족합니다."))
                            .authenticationEntryPoint(
                                    (req, res, ex) ->
                                            responseMsg(res, "로그인이 필요합니다."))
            );

        return http.build();
    }

    private void responseMsg(HttpServletResponse res, String msg) throws IOException {
        res.setStatus(401);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String body = objectMapper.writeValueAsString(ApiResponse.fail(msg));
        res.getWriter().write(body);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 