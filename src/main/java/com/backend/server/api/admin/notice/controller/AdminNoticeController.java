package com.backend.server.api.admin.notice.controller;

import com.backend.server.api.admin.notice.dto.AdminNoticeCreateRequest;
import com.backend.server.api.admin.notice.dto.AdminNoticeIdResponse;
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

@Tag(name = "관리자 공지사항", description = "관리자용 공지사항 관리 API")
@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

  private final AdminNoticeService adminNoticeService;

  @Operation(
      summary = "공지사항 등록",
      description = """
          새로운 공지사항을 등록합니다.
          
          **예시 URL:** http://localhost:8080/api/admin/notices
          """
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
      summary = "공지사항 목록 조회",
      description = """
          다양한 검색, 정렬, 필터 조건으로 공지사항 목록을 조회합니다.
          
          **검색 파라미터:**
          - adminNoticeSearchType: 검색 기준 (TITLE, CONTENT, ALL)
          - searchKeyword: 검색어
          
          **정렬 파라미터:**
          - sortBy: 정렬 기준 (ID, CREATED_AT 등)
          - direction: 정렬 방향 (ASC, DESC)
          
          **페이징 파라미터:**
          - page: 페이지 번호 (0부터 시작)
          - size: 한 페이지에 보여줄 개수
          
          **예시 URL:**
          - 기본 조회: http://localhost:8080/api/admin/notices
          - 검색 조회: http://localhost:8080/api/admin/notices?adminNoticeSearchType=TITLE&searchKeyword=기말고사
          - 정렬 및 페이징: http://localhost:8080/api/admin/notices?page=0&size=10&sortBy=ID&direction=DESC
          """
  )
  @GetMapping
  public ApiResponse<NoticeListResponse> getNotices(
      @Parameter(description = "공지사항 목록 조회 요청 DTO")
      @ParameterObject AdminNoticeListRequest request
  ) {
    return ApiResponse.success("공지사항 조회 성공", adminNoticeService.getNotices(request));
  }

  @Operation(
      summary = "공지사항 상세조회",
      description = """
          공지사항 ID를 이용하여 공지사항의 상세 정보를 조회합니다.
          
          예시 URL: http://localhost:8080/api/admin/notices/1
          """
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
      description = """
          기존 공지사항의 정보를 수정합니다.
          
          **예시 URL:** http://localhost:8080/api/admin/notices/1
          """
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
      description = """
          공지사항을 삭제합니다.
          
          **예시 URL:** http://localhost:8080/api/admin/notices/1
          """
  )
  public ApiResponse<Long> deleteNotice(
      @Parameter(description = "공지사항 ID")
      @PathVariable Long id
  ) {
    return ApiResponse.success("공지사항 삭제 성공", adminNoticeService.deleteNotice(id));
  }
}
