package com.backend.server.api.common.notification.controller;

import java.util.List;

import com.backend.server.api.common.notification.dto.NotificationIdResponse;
import com.backend.server.config.security.AccessTokenValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.data.domain.Pageable;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.notification.dto.CommonNotificationResponse;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.config.SseEmitterService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "알림", description = "알림 API")
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationSseController {

    private final SseEmitterService sseEmitterService;
    private final CommonNotificationService notificationService;
    private final AccessTokenValidator accessTokenValidator;
    //구독과 좋아용 그리고 알림설정 부탁드려요요
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam("access_token") String token) {
        try {
            if (!accessTokenValidator.validateAccessToken(token)) {
                throw new BadCredentialsException("유효하지 않은 토큰입니다.");
            }

            Long userId = accessTokenValidator.getUserIdByAccessToken(token);
            return sseEmitterService.createEmitter(userId);
        } catch (Exception e) {
            // 실패해도 빈 emitter로 연결은 끊어지지 않게 함
            SseEmitter emitter = new SseEmitter(1000L);
            emitter.completeWithError(e);
            return emitter;
        }
    }
    //알림 안읽은거 띄움
    @GetMapping
    @Operation(
            summary = "안 읽은 알림 조회 ",
            description = """
            로그인한 사용자의 안 읽은 알림을 최신순으로 조회합니다.  

            예시 요청:
            ```
            GET /api/notifications
            ```
            """
    )
    public ApiResponse<List<CommonNotificationResponse>> getAllNotReadNotification(@AuthenticationPrincipal LoginUser loginUser){
        return ApiResponse.success("안읽은 알림 다 봤어요", notificationService.getAllNotReadNotification(loginUser.getId()));
    }

    @PutMapping
    @Operation(
            summary = "알람 읽음 처리. 한개씩만 하구요 Response는 성공한 id 반환 .",
            description = """
            알람의 상태를 읽음으로 바꿔요.  

            예시 요청:
            ```
            PUT /api/notifications
            ```
            """
    )
    public ApiResponse<Long> changeIsReadTrue(@PathVariable Long id){
        return ApiResponse.success("안읽은 알림 다 봤어요", notificationService.changeIsReadTrue(id));
    }
    @Operation(
        summary = "모든 알림 조회 (페이징) / 혹시 몰라 만들었어요",
        description = """
            로그인한 사용자의 알림을 최신순으로 조회합니다.  
            페이징 정보는 `page`, `size`, `sort` 쿼리 파라미터로 전달해야합니다.
    
            예시 요청:
            ```
            GET /api/notifications/all?page=0&size=5&sort=createdAt,desc
            ```
            """
    )
    @GetMapping("/all")
    public ApiResponse<List<CommonNotificationResponse>> getAllNotReadNotification(
            @AuthenticationPrincipal LoginUser loginUser,
            Pageable pageable) {

        return ApiResponse.success(
            "모든 알림을 조회했습니다.",
            notificationService.getAllNotification(loginUser.getId(), pageable)
        );
    }
}
