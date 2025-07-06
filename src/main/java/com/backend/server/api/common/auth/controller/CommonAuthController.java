package com.backend.server.api.common.auth.controller;

import com.backend.server.api.common.auth.dto.CommonSignInRequest;
import com.backend.server.api.common.auth.dto.CommonSignInResponse;
import com.backend.server.api.common.auth.service.CommonAuthService;
import com.backend.server.api.common.dto.CommonResponse;
import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            description = """
        사용자가 로그인하면 엑세스 토큰을 반환합니다.<br/>

        <b>엑세스 토큰 정보:</b><br>
        - JWT 형식의 토큰이 발급됩니다.<br>
        - 토큰 유효 기간은 <code>15분</code>입니다.<br>

        <b>응답 코드:</b><br>
        - <code>200 OK</code>: 로그인 성공<br>
        - <code>500 Internal Server Error</code>: 서버 오류 발생<br>

        <hr/>

        <b>※ 로그인과 관련된 공통 에러 코드 안내</b><br>
        - <code>401 Unauthorized</code>: 로그인하지 않고 보호된 API에 접근한 경우<br>
        - <code>408 Request Timeout</code>: JWT 토큰이 만료된 경우<br>
        """
    )
    @PostMapping("/sign-in")
    public CommonResponse<CommonSignInResponse> signIn(
            @RequestBody CommonSignInRequest request) {
        return CommonResponse.success("로그인 성공", commonAuthService.login(request));
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
    public CommonResponse<Object> signOut(@AuthenticationPrincipal LoginUser loginUser) {
        commonAuthService.logout(loginUser);
        return CommonResponse.success("로그아웃 성공", null);
    }
}