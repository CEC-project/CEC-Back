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
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(description = "SSE서버 구독(로그인 시 처음 1회만 필요)")
    public SseEmitter subscribe(@RequestParam("accessToken") String token) {
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
            summary = "안 읽은 알림 전체 조회 "
    )
    public ApiResponse<List<CommonNotificationResponse>> getAllNotReadNotification(@AuthenticationPrincipal LoginUser loginUser){
        return ApiResponse.success("안읽은 알림 다 봤어요", notificationService.getAllNotReadNotification(loginUser.getId()));
    }


    @PatchMapping("/{id}")
    @Operation(summary = "단일 알림 읽음 처리")
    public ApiResponse<Long> markNotificationAsRead(@PathVariable Long id) {
        Long updatedId = notificationService.changeIsReadTrue(id);
        return ApiResponse.success("알림을 읽음 처리했습니다.", updatedId);
    }


}
