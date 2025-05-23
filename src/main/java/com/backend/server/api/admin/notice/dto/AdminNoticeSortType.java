package com.backend.server.api.admin.notice.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminNoticeSortType implements SortTypeConvertible {
    ID("id");

    private final String field;
}
