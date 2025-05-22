package com.backend.server.config.security;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Profile("!prod")
@Component
@RequiredArgsConstructor
public class DevAuthenticationFilter extends AbstractAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 엑세스 토큰이 없다면 DB 에서 ROLE_SUPER_ADMIN 을 찾아서 인증처리함
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorizationHeader)
                || !authorizationHeader.startsWith(BEARER_PREFIX)
                || authorizationHeader.length() == BEARER_PREFIX.length()) {

            List<User> users = userRepository.findByRoleIn(Role.ROLE_SUPER_ADMIN, Role.ROLE_ADMIN);
            if (users.isEmpty())
                throw new UsernameNotFoundException("DB 에서 관리자 계정을 찾을수 없음");

            User user = users.get(0);
            LoginUser loginUser = new LoginUser(user);

            var authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
            var authentication = new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        // 엑세스 토큰이 존재하므로 정상적인 인증처리함
        try {
            // ✅ CSRF 방지: Referer 검사
            String referer = request.getHeader("Referer");
            List<String> trustedDomains = List.of(
                    "http://localhost:3000", "http://localhost:3001", "http://localhost:8080",
                    "https://bmvcec.store", "https://admin.bmvcec.store",
                    "https://api.bmvcec.store", "https://dev.api.bmvcec.store"
            );
            if (referer == null || trustedDomains.stream().noneMatch(referer::startsWith)) {
                failAuth(request, response, referer + "Invalid request origin");
                return;
            }

            String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
            Long id = jwtUtil.getUserIdByAccessToken(accessToken);

            User user = userRepository
                    .findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("DB 에서 사용자를 찾을수 없음"));
            Role role = user.getRole();
            LoginUser loginUser = new LoginUser(user);

            var authorities = List.of(new SimpleGrantedAuthority(role.name()));
            var authentication = new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e) {
            // jwt 의 만료시간이 다 된 경우
            setResponse(response, "JWT expired", HttpServletResponse.SC_REQUEST_TIMEOUT);
        }
        catch (UsernameNotFoundException e) {
            // db 에서 사용자를 찾을 수 없는 경우
            failAuth(request, response, "User not found");
        }
        catch (Exception e) {
            // jwt 바디의 서명이나 내용이 유효하지 않은 경우
            failAuth(request, response, "JWT signature or content is invalid.");
        }
    }

    private void failAuth(HttpServletRequest req, HttpServletResponse res, String msg) throws IOException {
        log.error("{} {}", msg, getLog(req));
        setResponse(res, msg, HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void setResponse(HttpServletResponse res, String msg, int status) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        String body = objectMapper.writeValueAsString(ApiResponse.fail(msg));
        res.getWriter().write(body);
    }

    private String getLog(HttpServletRequest request) {
        return String.format("Method=%s, URI=%s, UA=%s",
                request.getMethod(),
                request.getRequestURI(),
                request.getHeader("User-Agent"));
    }
}