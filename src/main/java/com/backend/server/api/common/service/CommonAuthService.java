package com.backend.server.api.common.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.dto.auth.CommonSignInRequest;
import com.backend.server.api.common.dto.auth.CommonSignInResponse;
import com.backend.server.config.security.JwtUtil;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CommonAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommonSignInResponse login(HttpServletResponse response, CommonSignInRequest request) {
        Optional<User> optionalUser = userRepository.findByStudentNumber(request.getStudentNumber());
        if (optionalUser.isEmpty())
            throw new RuntimeException("로그인 실패");

        User user = optionalUser.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("로그인 실패");

        // 리프레시 토큰 중복 저장 방지
        String oldRefresh = jwtUtil.getRefreshToken(user.getId());
        if (StringUtils.hasText(oldRefresh))
            jwtUtil.deleteRefreshToken(oldRefresh, user.getId());

        String refresh = jwtUtil.createRefreshToken();
        jwtUtil.saveRefreshToken(refresh, user.getId());
        jwtUtil.saveRefreshCookie(response, refresh);

        return new CommonSignInResponse(user, jwtUtil.createAccessToken(user.getId()));
    }

    @Transactional
    public CommonSignInResponse refresh(String refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken))
            throw new JwtException("리프레시 토큰이 올바르지 않거나 만료되었습니다. 다시 로그인 하세요.");

        Long userid = jwtUtil.getUserIdByRefreshToken(refreshToken);
        if (userid == null)
            throw new RuntimeException("레디스에서 찾을 수 없는 리프레시 토큰입니다.");

        Optional<User> optionalUser = userRepository.findById(userid);
        if (optionalUser.isEmpty())
            throw new RuntimeException("DB 에 유저가 존재하지 않습니다.");

        User user = optionalUser.get();
        return new CommonSignInResponse(user, jwtUtil.createAccessToken(user.getId()));
    }

    @Transactional
    public void logout(LoginUser loginUser, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshToken(loginUser.getId());
        jwtUtil.deleteRefreshToken(refreshToken, loginUser.getId());
        jwtUtil.deleteRefreshCookie(response);
    }
}
