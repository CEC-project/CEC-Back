package com.backend.server.api.admin.inquiry.controller;

import com.backend.server.api.admin.inquiry.dto.AdminInquiryAnswerRequest;
import com.backend.server.api.admin.inquiry.dto.AdminInquiryListRequest;
import com.backend.server.api.admin.inquiry.dto.AdminInquiryListResponse;
import com.backend.server.api.admin.inquiry.service.AdminInquiryService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/inquiry")
@RequiredArgsConstructor
@Tag(name = "2-3. 사용자 관리 / 1:1 문의 목록", description = "수정 필요")
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @Operation(summary = "문의 목록 조회 API")
    @GetMapping("")
    public ApiResponse<AdminInquiryListResponse> getInquiries(@ParameterObject AdminInquiryListRequest request) {
        AdminInquiryListResponse result = adminInquiryService.getInquiries(request);
        return ApiResponse.success("문의 목록 조회 성공", result);
    }

    @Operation(summary = "문의 답변 등록 API")
    @PostMapping("/{id}")
    public ApiResponse<Long> addResponse(
            @PathVariable("id") Long inquiryId,
            @Valid @RequestBody AdminInquiryAnswerRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        Long responderId = loginUser.getId();
        Long id = adminInquiryService.addResponse(inquiryId, request, responderId);
        return ApiResponse.success("문의 답변 등록 성공", id);
    }
}