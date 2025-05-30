package com.backend.server.api.user.inquiry.controller;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.inquiry.dto.InquiryRequest;
import com.backend.server.api.user.inquiry.dto.InquiryResponse;
import com.backend.server.api.user.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/boards/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping // POST, 글 쓰기
    public ResponseEntity<Long> createInquiry(
            @Valid @RequestBody InquiryRequest request, // 유효성 검사 적용
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long currentUserId = loginUser.getId();
        Long inquiryId = inquiryService.createInquiry(request, currentUserId);
        return ResponseEntity.ok(inquiryId);
    }

    @GetMapping("/{id}") // GET, 상세 글 조회
    public ResponseEntity<InquiryResponse> getInquiry(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser
    ) throws AccessDeniedException {
        Long currentUserId = loginUser.getId();
        InquiryResponse response = inquiryService.getInquiry(id, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping // GET, 내 문의글 전체 조회
    public ResponseEntity<List<InquiryResponse>> getMyInquiries(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long currentUserId = loginUser.getId();
        List<InquiryResponse> responses = inquiryService.getMyInquiries(currentUserId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}") // PUT, 문의글 수정
    public ResponseEntity<Void> updateInquiry(
            @PathVariable Long id,
            @Valid @RequestBody InquiryRequest request, // 유효성 검사 적용
            @AuthenticationPrincipal LoginUser loginUser
    ) throws AccessDeniedException {
        Long currentUserId = loginUser.getId();
        inquiryService.updateInquiry(id, request, currentUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}") // DELETE, 문의글 삭제
    public ResponseEntity<Void> deleteInquiry(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser
    ) throws AccessDeniedException {
        Long currentUserId = loginUser.getId();
        inquiryService.deleteInquiry(id, currentUserId);
        return ResponseEntity.ok().build();
    }
}
