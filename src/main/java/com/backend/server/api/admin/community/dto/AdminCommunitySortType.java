package com.backend.server.api.admin.community.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminCommunitySortType implements SortTypeConvertible {
    ID("id"),
    RECOMMEND_COUNT("recommend"),
    VIEW_COUNT("view"),
    CREATED_AT("createdAt");

    private final String field;

    public static AdminCommunitySortType getDefault() { return AdminCommunitySortType.ID; }
}
