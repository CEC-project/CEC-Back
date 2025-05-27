package com.backend.server.api.admin.inquiry.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminInquirySortType implements SortTypeConvertible {
    NAME("name"),
    STUDENT_NUMBER("studentNumber"),
    CREATION_TIME("createdAt");

    private final String field;

    public static AdminInquirySortType getDefault() {
        return AdminInquirySortType.NAME;
    }
}
