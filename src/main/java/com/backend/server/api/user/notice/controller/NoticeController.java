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
      summary = "공지사항 목록 조회"
  )
  @GetMapping
  public ApiResponse<NoticeListResponse> getNotices(
      @Parameter(description = "공지사항 목록 조회 요청 DTO")
      @ParameterObject AdminNoticeListRequest request
  ) {
    return ApiResponse.success("공지사항 조회 성공", noticeService.getNotices(request));
  }

  @Operation(
      summary = "공지사항 단일조회"
  )
  @GetMapping("{id}")
  public ApiResponse<NoticeResponse> getNotice(
      @Parameter(description = "공지사항 ID")
      @PathVariable("id") Long noticeId
  ) {
    return ApiResponse.success("공지사항 조회 성공", noticeService.getNotice(noticeId));
  }
}
