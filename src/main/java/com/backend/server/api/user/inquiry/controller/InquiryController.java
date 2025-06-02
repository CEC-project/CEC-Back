package com.backend.server.api.user.inquiry.controller;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.inquiry.dto.InquiryRequest;
import com.backend.server.api.user.inquiry.dto.InquiryResponse;
import com.backend.server.api.user.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Tag(name = "문의 사항", description = "작업 완료")
@RestController
@RequestMapping("/api/boards/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;
    @Operation(summary = "문의글 작성", description = "인증된 사용자가 제목, 내용, 유형 등을 입력하여 문의글을 작성합니다.")
    @PostMapping // POST, 글 쓰기
    public ResponseEntity<Long> createInquiry(
            @Valid @RequestBody InquiryRequest request, // 유효성 검사 적용
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long currentUserId = loginUser.getId();
        Long inquiryId = inquiryService.createInquiry(request, currentUserId);
        return ResponseEntity.ok(inquiryId);
    }

    @Operation(summary = "문의글 상세 조회", description = "사용자가 본인이 작성한 문의글을 상세 조회합니다.")
    @GetMapping("/{id}") // GET, 상세 글 조회
    public ResponseEntity<InquiryResponse> getInquiry(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser
    ) throws AccessDeniedException {
        Long currentUserId = loginUser.getId();
        InquiryResponse response = inquiryService.getInquiry(id, currentUserId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 문의글 목록 조회", description = "로그인한 사용자의 모든 문의글을 페이지네이션과 함께 조회합니다.")
    @GetMapping // GET, 내 문의글 전체 조회 (페이지네이션 적용)
    public ResponseEntity<Page<InquiryResponse>> getMyInquiries( // 🔧 반환 타입 Page<>
                                                                 @AuthenticationPrincipal LoginUser loginUser,
                                                                 @RequestParam(defaultValue = "0") int page, // 🔧 page: 0부터 시작
                                                                 @RequestParam(defaultValue = "10") int size, // 🔧 size: 페이지당 개수
                                                                 @RequestParam(defaultValue = "createdAt") String sortBy, // 🔧 정렬 기준
                                                                 @RequestParam(defaultValue = "DESC") String sortDirection // 🔧 정렬 방향
    ) {
        Long currentUserId = loginUser.getId();
        Page<InquiryResponse> responses = inquiryService.getMyInquiries(
                currentUserId, page, size, sortBy, sortDirection // 🔧 서비스 호출 인자 맞춤
        );
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "문의글 수정", description = "본인이 작성한 문의글을 수정합니다.")
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

    @Operation(summary = "문의글 삭제", description = "본인이 작성한 문의글을 삭제합니다.")
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
