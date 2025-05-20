package com.backend.server.api.common.auth.controller;

import com.backend.server.api.common.auth.service.CommonAuthService;
import com.backend.server.api.common.dto.CommonResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.auth.dto.CommonSignInRequest;
import com.backend.server.api.common.auth.dto.CommonSignInResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "로그인/로그아웃/리프레시 API", description = "엑세스 토큰 / 리프레시 토큰 관련 API")
public class CommonAuthController {

    private final CommonAuthService commonAuthService;

    @Operation(
            summary = "로그인 API",
            description = "엑세스 토큰을 반환합니다. 엑세스 토큰은 15분만 지속됩니다.<br/>"
                    + "http 상태 코드는 200 / 500 을 반환합니다. - 아래 응답 예시 참고")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(examples = @ExampleObject(
                            """
                                    {
                                      "status": "success",
                                      "message": "로그인 성공",
                                      "data": {
                                        "id": 3,
                                        "studentNumber": "20230003",
                                        "nickname": "철수짱",
                                        "role": "ROLE_ADMIN",
                                        "department": "학과",
                                        "accessToken": "jwt헤더.jwt바디.jwt서명",
                                        "tokenType": "Bearer"
                                      }
                                    }"""
            ))),
            @ApiResponse(responseCode = "500", description = "DB에 없는 학번 or PW 틀림",
                    content = @Content(examples = @ExampleObject(
                            """
                                    {
                                      "status": "fail",
                                      "message": "2025-05-01T12:19:12.285+09:00 --- 로그인 실패",
                                      "data": null
                                    }"""
            )))})
    @PostMapping("/sign-in")
    public CommonResponse<CommonSignInResponse> signIn(
            HttpServletResponse response,
            @RequestBody CommonSignInRequest request) {
        return CommonResponse.success("로그인 성공", commonAuthService.login(response, request));
    }

    @Operation(
            summary = "엑세스 토큰 재발급 API",
            description = "스웨거에서는 사용하지 못하는 API 입니다.<br/>"
                    + "로그인 API 와 같은 구조로 응답합니다.<br/>"
                    + "http 상태 코드는 200 / 500 / 401 을 반환합니다. - 아래 응답 예시 참고"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 리프레시 성공",
                    content = @Content(examples = @ExampleObject(
                            """
                                    {
                                      "status": "success",
                                      "message": "토큰 리프레시 성공",
                                      "data": {
                                        "id": 3,
                                        "studentNumber": "20230003",
                                        "nickname": "철수짱",
                                        "role": "ROLE_ADMIN",
                                        "department": "학과",
                                        "accessToken": "jwt헤더.jwt바디.jwt서명",
                                        "tokenType": "Bearer"
                                      }
                                    }"""
                    ))),
            @ApiResponse(responseCode = "500", description = "DB에 없는 사용자 or 레디스에 없는 리프레시 토큰",
                    content = @Content(examples = @ExampleObject(
                            """
                                    {
                                      "status": "fail",
                                      "message": "2025-05-01T12:19:12.285+09:00 --- {오류난 이유}",
                                      "data": null
                                    }"""
                    ))),
            @ApiResponse(responseCode = "401", description = "리프레시 토큰이 만료되거나 올바르지 않음. 다시 로그인 해야함",
                    content = @Content(examples = @ExampleObject(
                                    """
                                    {
                                      "status": "fail",
                                      "message": "2025-05-01T12:19:12.285+09:00 --- {오류난 이유}",
                                      "data": null
                                    }"""
                    )))})
    @PostMapping("/token/refresh")
    public CommonResponse<CommonSignInResponse> refresh(
            @Parameter(hidden = true) @CookieValue("refreshToken") String refreshToken) {
        return CommonResponse.success("토큰 리프레시 성공", commonAuthService.refresh(refreshToken));
    }

    @Operation(
            summary = "로그아웃 API",
            description = "엑세스 토큰으로 로그아웃 처리합니다.<br/>"
                    + "프론트는 로그아웃 시 엑세스 토큰을 삭제해야 합니다."
    )
    @DeleteMapping("/sign-out")
    public CommonResponse<Object> signOut(@AuthenticationPrincipal LoginUser loginUser, HttpServletResponse response) {
        commonAuthService.logout(loginUser, response);
        return CommonResponse.success("로그아웃 성공", null);
    }
}