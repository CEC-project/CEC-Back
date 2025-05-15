package com.backend.server.api.common.notification.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationSseController {

    private final SseEmitterService sseEmitterService;
    private final CommonNotificationService notificationService;

    //구독과 좋아용 그리고 알림설정 부탁드려요요
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal LoginUser loginUser) {
        return sseEmitterService.createEmitter(loginUser.getId());
    }
    //알림 안읽은거 띄움
    @GetMapping
    public ApiResponse<List<CommonNotificationResponse>> getAllNotReadNotification(@AuthenticationPrincipal LoginUser loginUser){
        return ApiResponse.success("안읽은 알림 다 봤어요", notificationService.getAllNotReadNotification(loginUser.getId()));
    }
    @Operation(
        summary = "모든 알림 조회 (페이징)",
        description = """
            로그인한 사용자의 알림을 최신순으로 조회합니다.  
            페이징 정보는 `page`, `size`, `sort` 쿼리 파라미터로 전달하세요.
    
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
