package com.backend.server.api.admin.classroom.dto;

import com.backend.server.config.annotation.EnumValidator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminClassroomSearchRequest {
    public enum SearchType {
        ID, NAME, DESCRIPTION, ALL
    }

    private String keyword;

    @EnumValidator(enumClass = SearchType.class, message = "검색타입은 (ID, NAME, DESCRIPTION, ALL) 중 하나입니다.")
    private String type;

    public SearchType getSearchType() {
        return SearchType.valueOf(type);
    }
}