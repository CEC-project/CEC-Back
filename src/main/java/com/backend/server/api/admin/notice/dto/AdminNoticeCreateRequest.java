package com.backend.server.api.admin.notice.dto;

import com.backend.server.model.entity.Notice;
import com.backend.server.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminNoticeCreateRequest {

  @Schema(description = "공지사항 제목", example = "2025학년 1학기 기말고사 안내")
  @NotBlank(message = "공지사항 이름은 필수 입력 항목입니다.")
  private String title;

  @Schema(description = "공지사항 내용", example = "기말고사 기간 5/26 ~ 5/30")
  @NotBlank(message = "공지사항 내용은 필수 입력 항목입니다.")
  private String content;

  @Schema(description = "공지사항 중요도", example = "true")
  @NotNull(message = "공지사항 중요도는 필수 입력 항목입니다.")
  private Boolean important;

  @Schema(description = "첨부파일 URL (첨부파일 경로)")
  private List<String> attachments;

  public Notice toEntity(User author) {
    return Notice.builder()
        .title(title)
        .content(content)
        .important(important)
        .attachmentUrl(StringUtils.join(attachments, ";"))
        .view(0)
        .author(author)
        .build();
  }
}