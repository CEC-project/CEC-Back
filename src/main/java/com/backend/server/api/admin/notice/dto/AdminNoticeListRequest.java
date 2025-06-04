package com.backend.server.api.admin.notice.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class AdminNoticeListRequest extends AbstractPaginationParam {

  @Schema(description = "관리자 공지사항 검색 기준", implementation = AdminNoticeSearchType.class)
  AdminNoticeSearchType searchType;

  @Schema(description = "검색어")
  String searchKeyword;

  AdminNoticeSortType sortBy;

  public AdminNoticeListRequest(
      AdminNoticeSearchType searchType,
      String searchKeyword,

      Integer page,
      Integer size,
      AdminNoticeSortType sortBy,
      Sort.Direction direction
  ) {
    this.searchType = searchType == null ? AdminNoticeSearchType.ALL : searchType;
    this.searchKeyword = searchKeyword;

    this.page = Math.max(page, 0);
    this.size = Math.max(size, 10);
    this.sortBy = sortBy == null ? AdminNoticeSortType.ID : sortBy;
    this.sortDirection = direction == null ? Sort.Direction.ASC : direction;
  }

  public Pageable toPageable() {
    return toPageable(sortBy);
  }
}
