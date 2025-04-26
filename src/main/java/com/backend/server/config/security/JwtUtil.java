package com.backend.server.config.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.jwt.secret:'111111'}")
    private String secret;

    @Value("${access-token-validity-in-minutes:15}")
    private long accessTokenValidityInMinutes;

    @Value("${refresh-token-validity-in-days:14}")
    private long refreshTokenValidityInDays;

    private long accessTokenValidity;
    private long refreshTokenValidity;
    private Duration refreshTokenDuration;
    private JwtParser jwtParser;
    private Key key;

    @PostConstruct
    public void init() {
        accessTokenValidity = accessTokenValidityInMinutes * 60 * 1000;
        refreshTokenValidity = refreshTokenValidityInDays * 24 * 60 * 60 * 1000;
        refreshTokenDuration = Duration.ofMillis(accessTokenValidity);

        byte[] secretBytes = secret.getBytes();
        jwtParser = Jwts.parserBuilder().setSigningKey(secretBytes).build();
        key = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String createAccessToken(Long id) {
        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    public void saveRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/token/refresh")
                .maxAge(refreshTokenDuration)
                .build();
        response.addHeader("Set-Cookie", cookie.getValue());
    }

    public void deleteRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/token/refresh")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.getValue());
    }

    public void saveRefreshToken(String token, Long id) {
        redisTemplate.opsForValue().set("refreshToken:" + token, id.toString(), refreshTokenDuration);
        redisTemplate.opsForValue().set("refreshUser:" + id, token, refreshTokenDuration);
    }

    public void deleteRefreshToken(String token, Long id) {
        redisTemplate.delete("refreshToken:" + token);
        redisTemplate.delete("refreshUser:" + id);
    }

    public String getRefreshToken(Long id) {
        return redisTemplate.opsForValue().get("refreshUser:" + id);
    }

    public Long getUserIdByRefreshToken(String token) {
        String redisKey = "refreshToken:" + token;
        String value = redisTemplate.opsForValue().get(redisKey);
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
}