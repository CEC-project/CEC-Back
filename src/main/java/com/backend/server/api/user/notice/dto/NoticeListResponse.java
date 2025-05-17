package com.backend.server.api.user.notice.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoticeListResponse {
  private List<NoticeResponse> content;
  private PageableInfo pageable;

  public NoticeListResponse(Page<Notice> page) {
    this.content = page.getContent().stream()
        .map(NoticeResponse::new)
        .toList();
    this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
  }
}
