package com.backend.server.api.admin.rentalRestriction.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminRentalRestrictionSortType implements SortTypeConvertible {
    ID("id", true),
    NAME("name", true),
    STUDENT_NUMBER("studentNumber", true),
    RESTRICTION_COUNT("restrictionCount", true),
    RESTRICTION_START_TIME("createdAt", false),
    RESTRICTION_END_TIME("endAt", false);

    private final String field;
    private final boolean userTable;

    public static AdminRentalRestrictionSortType getDefault() {
        return AdminRentalRestrictionSortType.NAME;
    }
}
