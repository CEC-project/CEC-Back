package com.backend.server.api.user.notice.dto;

import com.backend.server.api.common.dto.AuthorResponse;
import com.backend.server.model.entity.Notice;
import com.backend.server.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponse {

  @Schema(description = "공지사항 ID", example = "1")
  private Long id;

  @Schema(description = "공지사항 제목", example = "시스템 점검 안내")
  private String title;

  @Schema(description = "공지사항 본문 내용", example = "시스템 점검이 6월 3일 00시에 진행됩니다.")
  private String content;

  // important 로 통일하려고 합니다. -qkr10
  @Schema(description = "중요 공지 여부 (true이면 상단 고정)", example = "true")
  private Boolean important;

  @Schema(description = "첨부 파일 URL", example = "https://example.com/files/manual.pdf", nullable = true)
  private String attachmentUrl;

  @Schema(description = "공지사항 조회수", example = "132")
  private Integer view;

  @Schema(description = "공지 등록 시간", example = "2025-06-01T12:00:00")
  private LocalDateTime createdAt;

  @Schema(description = "공지 수정 시간", example = "2025-06-01T14:30:00")
  private LocalDateTime updatedAt;

  @Schema(description = "공지 작성자 정보")
  private AuthorResponse author;

  public NoticeResponse(Notice notice) {
    User user = notice.getAuthor();

    this.id = notice.getId();
    this.title = notice.getTitle();
    this.content = notice.getContent();
    this.important = notice.getImportant();
    this.attachmentUrl = notice.getAttachmentUrl();
    this.view = notice.getView();
    this.createdAt = notice.getCreatedAt();
    this.updatedAt = notice.getUpdatedAt();

    this.author = user == null ? null : AuthorResponse.from(user);
  }
}
