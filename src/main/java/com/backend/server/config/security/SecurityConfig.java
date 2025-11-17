package com.backend.server.config.security;

import com.backend.server.api.common.dto.ApiResponse;
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

    private final AbstractAuthenticationFilter authenticationFilter;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestMatcher requestMatcher = new OrRequestMatcher(
                AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                AntPathRequestMatcher.antMatcher("/swagger-resources"),
                AntPathRequestMatcher.antMatcher("/webjars/**"),
                AntPathRequestMatcher.antMatcher("/configuration/ui"),
                AntPathRequestMatcher.antMatcher("/configuration/security"),
                AntPathRequestMatcher.antMatcher("/api/auth/sign-in"),
                AntPathRequestMatcher.antMatcher("/api/auth/token/refresh"),
                AntPathRequestMatcher.antMatcher("/api/health-check")
        );

        List<String> origins = List.of(
                "http://localhost:3000", "http://localhost:3001", "http://localhost:8080",
                "https://bmvcec.store", "https://admin.bmvcec.store",
                "https://api.bmvcec.store", "https://dev.api.bmvcec.store"
        );

        http
            .csrf(AbstractHttpConfigurer::disable)

            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(origins); // 모든 Origin 허용
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(requestMatcher).permitAll()
                    .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                    .requestMatchers("/api/notifications/subscribe").permitAll()

                    .anyRequest().hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")
            )
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
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