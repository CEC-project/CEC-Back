package com.backend.server.api.common.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.dto.auth.CommonSignInRequest;
import com.backend.server.api.common.dto.auth.CommonSignInResponse;
import com.backend.server.api.common.service.CommonAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class CommonAuthController {

    private final CommonAuthService commonAuthService;

    @Operation(
            summary = "로그인 API",
            description = "엑세스 토큰을 반환합니다. 엑세스 토큰은 15분만 지속됩니다.\\n"
                    + "스웨거로 테스트 할때 15분마다 로그인하기 귀찮으면, 설정파일에서 액세스 토큰 시간을 늘려주세요."
    )
    @PostMapping("/sign-in")
    public ApiResponse<CommonSignInResponse> signIn(
            HttpServletResponse response,
            @RequestBody CommonSignInRequest request) {
        return ApiResponse.success("로그인 성공", commonAuthService.login(response, request));
    }

    @Operation(
            summary = "엑세스 토큰 재발급 API",
            description = "스웨거에서는 사용하지 못하는 API 입니다."
    )
    @PostMapping("/token/refresh")
    public ApiResponse<CommonSignInResponse> refresh(
            @Parameter(hidden = true) @CookieValue("refreshToken") String refreshToken) {
        return ApiResponse.success("토큰 리프레시 성공", commonAuthService.refresh(refreshToken));
    }

    @Operation(
            summary = "로그아웃 API",
            description = "엑세스 토큰으로 로그아웃 처리합니다.\\n"
                    + "프론트는 로그아웃 시 엑세스 토큰을 삭제해야 합니다."
    )
    @DeleteMapping("/sign-out")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<Object> signOut(@AuthenticationPrincipal LoginUser loginUser, HttpServletResponse response) {
        commonAuthService.logout(loginUser, response);
        return ApiResponse.success("로그아웃 성공", null);
    }
}