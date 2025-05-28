package com.backend.server.api.admin.classroom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminClassroomSearchRequest {
    public enum SearchType {
        ID, NAME, DESCRIPTION, ALL
    }

    private String keyword;

    private SearchType type;
}