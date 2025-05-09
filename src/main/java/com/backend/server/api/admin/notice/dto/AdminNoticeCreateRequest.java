package com.backend.server.api.admin.notice.dto;

import com.backend.server.model.entity.Notice;
import com.backend.server.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "공지사항 생성 요청 DTO")
public record AdminNoticeCreateRequest(
    @Schema(description = "공지사항 제목", example = "2025학년 1학기 기말고사 안내")
    @NotBlank(message = "공지사항 이름은 필수 입력 항목입니다.")
    String title,

    @Schema(description = "공지사항 내용", example = "기말고사 기간 5/26 ~ 5/30")
    @NotBlank(message = "공지사항 내용은 필수 입력 항목입니다.")
    String content,

    @Schema(description = "공지사항 중요도", example = "true")
    @NotBlank(message = "공지사항 중요도는 필수 입력 항목입니다.")
    Boolean important,

    @Schema(description = "첨부파일 URL (첨부파일 경로)", example = "images/exam_day.jpg")
    String attachmentUrl
) {
  public Notice toEntity(User author) {
    return Notice.builder()
        .title(title)
        .content(content)
        .important(important)
        .attachmentUrl(attachmentUrl)
        .author(author)
        .build();
  }
}
