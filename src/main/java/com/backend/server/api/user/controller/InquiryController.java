package com.backend.server.api.user.controller;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.dto.inquiry.InquiryRequest;
import com.backend.server.api.user.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping // POST
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')") // User, Admin, SuperAdmin만 가능
    public ResponseEntity<Long> createInquiry(
            @RequestBody InquiryRequest request,
            @AuthenticationPrincipal LoginUser loginUser  // 인증된 사용자 정보 주입
    ) {
        Long currentUserId = loginUser.getId(); // 로그인 사용자 ID 가져오기
        Long inquiryId = inquiryService.createInquiry(request, currentUserId); // DB 저장
        return ResponseEntity.ok(inquiryId);
    }
}