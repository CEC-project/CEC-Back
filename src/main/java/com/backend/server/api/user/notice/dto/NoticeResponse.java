package com.backend.server.api.user.notice.dto;

import com.backend.server.api.common.dto.ProfileResponse;
import com.backend.server.api.user.mypage.dto.MyInfoResponse;
import com.backend.server.model.entity.Notice;
import com.backend.server.model.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponse {
  private Long id;
  private String title;
  private String content;
  private Boolean isImportant;
  private String attachmentUrl;
  private Integer view;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private ProfileResponse author;

  public NoticeResponse(Notice notice) {
    User user = notice.getAuthor();

    this.id = notice.getId();
    this.title = notice.getTitle();
    this.content = notice.getContent();
    this.isImportant = notice.getImportant();
    this.attachmentUrl = notice.getAttachmentUrl();
    this.view = notice.getView();
    this.createdAt = notice.getCreatedAt();
    this.updatedAt = notice.getUpdatedAt();

    this.author = new ProfileResponse(
            user.getId(),
            user.getName(),
            user.getNickname(),
            user.getProfilePicture(),
            user.getRole().name()
    );
  }
}
