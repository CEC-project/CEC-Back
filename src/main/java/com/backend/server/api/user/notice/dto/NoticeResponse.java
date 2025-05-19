package com.backend.server.api.user.notice.dto;

import com.backend.server.model.entity.Notice;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponse {
  private Long id;
  private String title;
  private String content;
  private Boolean isImportant;
  private String attachmentUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Long authorId;
  private String authorName;
  private String authorNickname;
  private String authorProfilePicture;

  public NoticeResponse(Notice notice) {
    this.id = notice.getId();
    this.title = notice.getTitle();
    this.content = notice.getContent();
    this.isImportant = notice.getImportant();
    this.attachmentUrl = notice.getAttachmentUrl();
    this.createdAt = notice.getCreatedAt();
    this.updatedAt = notice.getUpdatedAt();

    this.authorId = notice.getAuthor().getId();
    this.authorName = notice.getAuthor().getName();
    this.authorNickname = notice.getAuthor().getNickname();
    this.authorProfilePicture = notice.getAuthor().getProfilePicture();
  }
}
