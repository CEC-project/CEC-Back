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

    //이거는 왜 만들었냐? 쿼리파라미터로 부득이하게 넘겨야 하는 상황 sse 등.. 에서 쓰려고 만들었어요

    @PostConstruct
    public void init() {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build();
    }
    public boolean validateAccessToken(String accessToken) {
        try {
            jwtParser.parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이라면 false 혹은 예외 재던지기
            return false;
        } catch (JwtException e) {
            // 서명 불일치 등
            return false;
        }
    }

    public Long getUserIdByAccessToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
