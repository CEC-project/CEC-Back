package com.backend.server.api.admin.notice.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Notice;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class AdminNoticeListResponse {
  private List<AdminNoticeResponse> content;
  private PageableInfo pageable;

  public AdminNoticeListResponse(Page<Notice> page) {
    this.content = page.getContent().stream()
        .map(AdminNoticeResponse::new)
        .toList();
    this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
  }
}
