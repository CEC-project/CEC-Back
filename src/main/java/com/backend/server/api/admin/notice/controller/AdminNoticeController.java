package com.backend.server.api.admin.notice.controller;

import com.backend.server.api.admin.notice.dto.AdminNoticeCreateRequest;
import com.backend.server.api.admin.notice.dto.AdminNoticeIdResponse;
import com.backend.server.api.admin.notice.service.AdminNoticeService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 공지사항", description = "관리자용 공지사항 관리 API")
@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

  private final AdminNoticeService adminNoticeService;

  @Operation(summary = "공지사항 등록", description = "새로운 공지사항를 등록합니다.")
  @PostMapping
  public ApiResponse<AdminNoticeIdResponse> createNotice(
      @Valid @RequestBody AdminNoticeCreateRequest request,
      @AuthenticationPrincipal LoginUser loginUser
  ) {
    return ApiResponse.success("공지사항 등록 성공", adminNoticeService.createNotice(request, loginUser));
  }
}
