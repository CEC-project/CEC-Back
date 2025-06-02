package com.backend.server.api.admin.user.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminUserSortType implements SortTypeConvertible {
    ID("id"),
    NAME("name"),
    STUDENT_NUMBER("studentNumber"),
    RENTAL_COUNT("rentalCount"),
    BROKEN_COUNT("damageCount"),
    RESTRICTION_COUNT("restrictionCount"),
    REPORT_COUNT("reportCount");

    private final String field;

    public static AdminUserSortType getDefault() {
        return AdminUserSortType.ID;
    }
}
