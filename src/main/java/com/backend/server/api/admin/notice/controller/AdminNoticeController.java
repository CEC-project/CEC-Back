package com.backend.server.api.admin.notice.controller;

import com.backend.server.api.admin.notice.dto.AdminNoticeCreateRequest;
import com.backend.server.api.admin.notice.dto.AdminNoticeListRequest;
import com.backend.server.api.admin.notice.service.AdminNoticeService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.notice.dto.NoticeListResponse;
import com.backend.server.api.user.notice.dto.NoticeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "5-1. 게시판 관리 / 공지사항", description = "1차 수정 완료")
@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

  private final AdminNoticeService adminNoticeService;

  @Operation(
      summary = "공지사항 등록"
  )
  @PostMapping
  public ApiResponse<Long> createNotice(
      @Parameter(description = "공지사항 생성 요청 DTO")
      @Valid @RequestBody AdminNoticeCreateRequest request,
      @AuthenticationPrincipal LoginUser loginUser
  ) {
    return ApiResponse.success("공지사항 등록 성공", adminNoticeService.createNotice(request, loginUser));
  }

  @Operation(
      summary = "공지사항 목록 조회"
          )
  @GetMapping
  public ApiResponse<NoticeListResponse> getNotices(
      @Parameter(description = "공지사항 목록 조회 요청 DTO")
      @ParameterObject AdminNoticeListRequest request
  ) {
    return ApiResponse.success("공지사항 조회 성공", adminNoticeService.getNotices(request));
  }

  @Operation(
      summary = "공지사항 상세조회"
  )
  @GetMapping("{id}")
  public ApiResponse<NoticeResponse> getNotice(
      @Parameter(description = "공지사항 ID")
      @PathVariable("id") Long noticeId
  ) {
    return ApiResponse.success("공지사항 조회 성공", adminNoticeService.getNotice(noticeId));
  }

  @PutMapping("{id}")
  @Operation(
      summary = "공지사항 수정",
      description = "기존 공지사항의 정보를 수정합니다."
  )
  public ApiResponse<Long> updateNotice(
      @Parameter(description = "공지사항 ID")
      @PathVariable Long id,
      @Parameter(description = "공지사항 수정 요청 DTO")
      @Valid @RequestBody AdminNoticeCreateRequest request
  ) {
    return ApiResponse.success("공지사항 수정 성공", adminNoticeService.updateNotice(id, request));
  }

  @DeleteMapping("{id}")
  @Operation(
      summary = "공지사항 삭제",
      description = "공지사항을 삭제합니다."
  )
  public ApiResponse<Long> deleteNotice(
      @Parameter(description = "공지사항 ID")
      @PathVariable Long id
  ) {
    return ApiResponse.success("공지사항 삭제 성공", adminNoticeService.deleteNotice(id));
  }
}
