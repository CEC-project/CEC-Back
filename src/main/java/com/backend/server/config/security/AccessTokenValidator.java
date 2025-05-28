package com.backend.server.config.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AccessTokenValidator {
    private JwtParser jwtParser;

    @Value("${spring.jwt.secret:111111}")
    private String secret;

    @Value("${test.token:test-token}")
    private String testToken;

    @PostConstruct
    public void init() {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build();
    }

    public boolean validateAccessToken(String accessToken) {
        // 테스트 토큰이면 true
        if (accessToken != null && accessToken.equals(testToken)) {
            return true;
        }

        try {
            jwtParser.parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    public Long getUserIdByAccessToken(String token) {
        if (token.equals(testToken)) {
            return 1L; //테스트 아이디 1임
        }

        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
