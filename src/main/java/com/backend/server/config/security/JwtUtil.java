package com.backend.server.config.security;

import com.backend.server.model.repository.cookie.CookieRepository;
import com.backend.server.model.repository.keyValue.KeyValueRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil{

    @Value("${spring.jwt.secret:111111}")
    private String secret;

    @Value("${test.token:test-token}")
    private String testToken;

    @Value("${spring.jwt.access-token-validity-in-minutes:15}")
    private long accessTokenValidityInMinutes;

    @Value("${spring.jwt.refresh-token-validity-in-days:14}")
    private long refreshTokenValidityInDays;

    private long accessTokenValidity;
    private long refreshTokenValidity;
    private Duration refreshTokenDuration;
    private JwtParser jwtParser;
    private SecretKey jwtKey;

    private final KeyValueRepository keyValueRepository;
    private final CookieRepository cookieRepository;

    @PostConstruct
    public void init() {
        accessTokenValidity = accessTokenValidityInMinutes * 60 * 1000;
        refreshTokenValidity = refreshTokenValidityInDays * 24 * 60 * 60 * 1000;
        refreshTokenDuration = Duration.ofMillis(refreshTokenValidity);

        byte[] secretBytes = secret.getBytes();
        jwtKey = Keys.hmacShaKeyFor(secretBytes);
        jwtParser = Jwts.parser().verifyWith(jwtKey).build();
    }

    public String createAccessToken(Long id) {
        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(jwtKey)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(jwtKey)
                .compact();
    }

    public void saveRefreshCookie(String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/api/auth/token/refresh")
                .maxAge(refreshTokenDuration)
                .build();
        cookieRepository.setCookie(cookie);
    }

    public void deleteRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/api/auth/token/refresh")
                .maxAge(0)
                .build();
        cookieRepository.setCookie(cookie);
    }

    public void saveRefreshToken(String token, Long id) {
        keyValueRepository.set("refreshToken:" + token, id.toString(), refreshTokenDuration);
        keyValueRepository.set("refreshUser:" + id, token, refreshTokenDuration);
    }

    public void deleteRefreshToken(String token, Long id) {
        keyValueRepository.delete("refreshToken:" + token);
        keyValueRepository.delete("refreshUser:" + id);
    }

    public String getRefreshToken(Long id) {
        return keyValueRepository.get("refreshUser:" + id);
    }

    public Long getUserIdByRefreshToken(String token) {
        String key = "refreshToken:" + token;
        String value = keyValueRepository.get(key);
        if (value == null)
            return null;
        return Long.valueOf(value);
    }

    public Long getUserIdByAccessToken(String token) {
        if (!jwtParser.isSigned(token))
            throw new JwtException("서명이 올바르지 않음");
        // 토큰 만료시 parseClaimsJws 에서 ExpiredJwtException throw
        String subject = jwtParser.parseClaimsJws(token).getBody().getSubject();
        return Long.parseLong(subject);
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            jwtParser.parseClaimsJws(refreshToken);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public boolean validateAccessToken(String accessToken) {
        // 테스트 토큰이면 true
        if (accessToken != null && accessToken.equals(testToken)) {
            return true;
        }

        try {
            jwtParser.parseClaimsJws(accessToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}