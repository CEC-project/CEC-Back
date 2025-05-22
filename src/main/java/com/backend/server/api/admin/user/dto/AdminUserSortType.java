package com.backend.server.api.admin.user.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminUserSortType implements SortTypeConvertible {
    NAME("name"),
    STUDENT_NUMBER("studentNumber"),
    RESTRICTION_COUNT("restrictionCount");

    private final String field;

    public static AdminUserSortType getDefault() {
        return AdminUserSortType.NAME;
    }
}
