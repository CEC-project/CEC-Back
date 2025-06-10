package com.backend.server.api.user.community.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunitySortType implements SortTypeConvertible {
    ID("id");

    private final String field;
}
