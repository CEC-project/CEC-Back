package com.backend.server.api.admin.notice.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminNoticeSortType implements SortTypeConvertible {
    ID("id"),
    //  NOTICE_AUTHOR("author.id") 연관관계를 사용한 정렬 시 예시코드
    EMPTY(null);

    private final String field;
}
