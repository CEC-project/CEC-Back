package com.backend.server.api.user.notice.controller;


import com.backend.server.api.admin.notice.dto.AdminNoticeListRequest;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.notice.dto.NoticeListResponse;
import com.backend.server.api.user.notice.dto.NoticeResponse;
import com.backend.server.api.user.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항", description = "공지사항 API")
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

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
    return ApiResponse.success("공지사항 조회 성공", noticeService.getNotices(request));
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
    return ApiResponse.success("공지사항 조회 성공", noticeService.getNotice(noticeId));
  }
}
