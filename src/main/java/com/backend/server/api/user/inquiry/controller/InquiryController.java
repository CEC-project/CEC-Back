package com.backend.server.api.user.inquiry.controller;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.inquiry.dto.InquiryRequest;
import com.backend.server.api.user.inquiry.dto.InquiryResponse;
import com.backend.server.api.user.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/boards/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping // POST, 글 쓰기
    public ResponseEntity<Long> createInquiry(
            @RequestBody InquiryRequest request,
            @AuthenticationPrincipal LoginUser loginUser  // 인증된 사용자 정보 주입
    ) {
        Long currentUserId = loginUser.getId(); // 로그인 사용자 ID 가져오기
        Long inquiryId = inquiryService.createInquiry(request, currentUserId); // DB 저장
        return ResponseEntity.ok(inquiryId);
    }

    @GetMapping("/{id}") //GET, 상세 글 조회
    public ResponseEntity<InquiryResponse> getInquiry(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser
    ) throws AccessDeniedException {
        Long currentUserId = loginUser.getId();
        InquiryResponse response = inquiryService.getInquiry(id, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping // GET, 전체 글 조회
    public ResponseEntity<List<InquiryResponse>> getMyInquiries(
            @AuthenticationPrincipal LoginUser loginUser
    ){
        Long currentUserId = loginUser.getId();
        List<InquiryResponse> responses = inquiryService.getMyInquiries(currentUserId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}") // PUT, 문의글 수정
    public ResponseEntity<Void> updateInquiry(
        @PathVariable Long id,
        @RequestBody InquiryRequest request,
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