package com.backend.server.api.user.equipment.dto.equipment;

import com.backend.server.api.common.dto.PageableRequest;
import com.backend.server.model.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import com.backend.server.api.common.dto.PageableRequest;
import com.backend.server.model.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "장비 목록 조회 요청 DTO")
public class EquipmentListRequest implements PageableRequest {
    @Schema(description = "카테고리 ID로 정렬", example = "1")
    private Long categoryId;
    @Schema(description = "검색 키워드 (모델명, 대여자명, 일련번호, 카테고리명)", example = "SONY")
    private String keyword;

    @Schema(description = "검색 타입 (ALL, MODEL_NAME, CATEGORY_NAME, SERIAL_NUMBER, RENTER_NAME)", example = "ALL")
    private SearchType searchType;

    @Schema(description = "장비 상태 (AVAILABLE, IN_USE, BROKEN 등)", example = "AVAILABLE")
    private Status status;


    // 페이징 및 정렬
    @Schema(description = "요청할 페이지 번호 (0부터 시작)", example = "0")
    private Integer page;

    @Schema(description = "페이지 당 항목 수", example = "20")
    private Integer size;

    @Schema(description = "정렬할 필드 (id, createdAt, name 등)", example = "id")
    private SortBy sortBy = SortBy.ID;

    @Schema(description = "정렬 방향 (ASC 또는 DESC)", example = "DESC")
    private SortDirection sortDirection;

    public enum SearchType {
        ALL, MODEL_NAME, CATEGORY_NAME, RENTER_NAME
    }

    public enum SortDirection {
        ASC, DESC
    }

    @Getter
    public enum SortBy {
        NAME("name"),
        ID("id");

        private final String field;
        SortBy(String field) { this.field = field; }
        public String getField() { return field; }
    }
    // PageableRequest 구현
    @Override public Integer getPage() { return page; }
    @Override public Integer getSize() { return size; }
    @Override public String getSortBy() { return sortBy.getField(); }
    @Override public String getSortDirection() { return sortDirection != null ? sortDirection.name() : "DESC"; }
}
