package com.backend.server.api.admin.equipment.dto.equipment.request;

import com.backend.server.api.common.dto.PageableRequest;

import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.model.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장비 목록 조회 요청 DTO")
public class AdminEquipmentListRequest implements PageableRequest {

    @Schema(description = "카테고리 ID로 정렬", example = "1")
    private Long categoryId;

    @Schema(description = "검색 키워드 (모델명, 일련번호, 대여자 이름 포함)", example = "SONY")
    private String searchKeyword;

    @Schema(description = "검색 타입 (ALL, CATEGORY_NAME, MODEL_NAME, SERIAL_NUMBER, RENTER_NAME 중 선택)", example = "ALL"
            ,implementation = SearchType.class)
    private SearchType searchType = SearchType.ALL;

    @Schema(description = "장비 상태 (ALL, AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING, RETURN_PENDING 중 선택)",
            example = "AVAILABLE"
            ,implementation = EquipmentStatus.class)
    private EquipmentStatus status = EquipmentStatus.ALL;

    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private Integer page;

    @Schema(description = "페이지당 항목 수", example = "17")
    private Integer size;

    @Schema(description = "정렬 기준 (ID, NAME, CREATED_AT, RENTAL_COUNT, REPAIR_COUNT )",
            example = "createdAt"
            ,implementation = SortBy.class)
    private SortBy sortBy = SortBy.ID;

    @Getter
    public enum SortBy {
        NAME("name"),
        ID("id"),
        CREATED_AT("createdAt"),
        RENTAL_COUNT("rentalCount"),
        REPAIR_COUNT("repairCount"),
        BROKEN_COUNT("brokenCount");

        private final String field;
        SortBy(String field) { this.field = field; }
        public String getField() { return field; }
    }

    @Schema(description = "정렬 순서 (ASC 또는 DESC)", example = "DESC", implementation = SortDirection.class)
    private SortDirection sortDirection = SortDirection.DESC;

    public enum SearchType {
        ALL, MODEL_NAME, CATEGORY_NAME , SERIAL_NUMBER, RENTER_NAME
    }

    public enum EquipmentStatus {
        ALL, AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING, RETURN_PENDING
    }

    public enum SortDirection {
        ASC, DESC
    }

    // 페이징 및 정렬 관련 인터페이스 구현
    @Override public Integer getPage() { return page; }
    @Override public Integer getSize() { return size; }
    @Override public String getSortBy() { return sortBy.getField(); }
    @Override public String getSortDirection() { return sortDirection.name(); }
}
