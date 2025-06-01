package com.backend.server.api.admin.classroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Getter
@Setter
public class AdminClassroomSearchRequest {

    public enum SearchType {
        ID, NAME, DESCRIPTION, ALL
    }

    public enum Status {
        ALL, AVAILABLE, IN_USE, CANCELABLE, BROKEN, RENTAL_PENDING
    }

    @Getter
    public enum SortBy {
        REQUESTED_TIME("requestedTime"),
        NAME("name"),
        ID("id"),
        DESCRIPTION("description"),
        STATUS("status");
        private final String field;
        SortBy(String field) {
            this.field = field;
        }
    }

    public enum SortDirection {
        ASC, DESC
    }

    @Schema(description = "검색어")
    private String keyword = "";

    @Schema(description = "검색 타입", implementation = SearchType.class)
    private SearchType type = SearchType.ALL;

    @Schema(description = "상태 필터", implementation = Status.class)
    private Status status = Status.ALL;

    @Schema(description = "정렬 기준", implementation = SortBy.class)
    private SortBy sortBy = SortBy.REQUESTED_TIME;

    @Schema(description = "정렬 방향", implementation = SortDirection.class)
    private SortDirection sortDirection = SortDirection.ASC;

    public Sort toSort() {
        return Sort.by(Direction.valueOf(sortDirection.name()), sortBy.getField());
    }
}