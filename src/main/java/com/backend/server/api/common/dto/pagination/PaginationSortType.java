package com.backend.server.api.common.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaginationSortType {
  ID("id"),
  //  NOTICE_AUTHOR("author.id") 연관관계를 사용한 정렬 시 예시코드
  EMPTY(null);

  public final String field;
}
