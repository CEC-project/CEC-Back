package com.backend.server.api.admin.rentalRestriction.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminRentalRestrictionSortType implements SortTypeConvertible {
    NAME("name"),
    STUDENT_NUMBER("studentNumber"),
    RESTRICTION_COUNT("restrictionCount"),
    RESTRICTION_START_TIME("rentalRestriction.createdAt"),
    RESTRICTION_END_TIME("rentalRestriction.endAt");

    private final String field;

    public static AdminRentalRestrictionSortType getDefault() {
        return AdminRentalRestrictionSortType.NAME;
    }
}
