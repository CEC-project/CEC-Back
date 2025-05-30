package com.backend.server.api.admin.notice.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class AdminNoticeListRequest extends AbstractPaginationParam<AdminNoticeSortType> {

  @Schema(description = "관리자 공지사항 검색 기준", implementation = AdminNoticeSearchType.class)
  AdminNoticeSearchType adminNoticeSearchType;

  @Schema(description = "검색어")
  String searchKeyword;

  public AdminNoticeListRequest(
      AdminNoticeSearchType adminNoticeSearchType,
      String searchKeyword,

      Integer page,
      Integer size,
      AdminNoticeSortType sortBy,
      Sort.Direction direction
  ) {
    this.adminNoticeSearchType = adminNoticeSearchType == null ? AdminNoticeSearchType.ALL : adminNoticeSearchType;
    this.searchKeyword = searchKeyword;

    this.page = Math.max(page, 0);
    this.size = Math.max(size, 10);
    this.sortBy = sortBy == null ? AdminNoticeSortType.ID : sortBy;
    this.direction = direction == null ? Sort.Direction.ASC : direction;
  }
}
