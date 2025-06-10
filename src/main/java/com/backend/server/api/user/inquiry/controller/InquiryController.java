package com.backend.server.api.user.inquiry.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.inquiry.dto.InquiryListResponse; // 명세에 맞춘 DTO import
import com.backend.server.api.user.inquiry.dto.InquiryRequest;
import com.backend.server.api.user.inquiry.dto.InquiryResponse;
import com.backend.server.api.user.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
@Tag(name = "3-2. 유저 프로필 / 1:1 문의하기", description = "문의 관련 API")
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "문의글 작성", description = "인증된 사용자가 제목, 내용, 유형 등을 입력하여 문의글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createInquiry(
            @Valid @RequestBody InquiryRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long currentUserId = loginUser.getId();
        Long inquiryId = inquiryService.createInquiry(request, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("문의글 작성 성공", inquiryId));
    }

    @Operation(summary = "문의글 상세 조회", description = "본인이 작성한 문의글 + 답변 내용")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InquiryResponse>> getInquiry(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser
    ) throws Exception {
        Long currentUserId = loginUser.getId();
        InquiryResponse response = inquiryService.getInquiry(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("문의글 조회 성공", response));
    }

    @Operation(summary = "내 문의글 목록 조회", description = "로그인한 사용자의 모든 문의글을 페이지네이션과 함께 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<InquiryListResponse>> getMyInquiries( // 수정: 반환 타입 Page → DTO
                                                                            @AuthenticationPrincipal LoginUser loginUser,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Long currentUserId = loginUser.getId();
        InquiryListResponse responses = inquiryService.getMyInquiries(currentUserId, page, size, sortBy, sortDirection); // 반환값 DTO
        return ResponseEntity.ok(ApiResponse.success("문의글 목록 조회 성공", responses));
    }

    @Operation(summary = "문의글 수정", description = "본인이 작성한 문의글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateInquiry(
            @PathVariable Long id,
            @Valid @RequestBody InquiryRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) throws Exception {
        Long currentUserId = loginUser.getId();
        inquiryService.updateInquiry(id, request, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("문의글 수정 성공", id));
    }

    @Operation(summary = "문의글 삭제", description = "본인이 작성한 문의글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInquiry(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser
    ) throws Exception {
        Long currentUserId = loginUser.getId();
        inquiryService.deleteInquiry(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("문의글 삭제 성공", null));
    }
}
