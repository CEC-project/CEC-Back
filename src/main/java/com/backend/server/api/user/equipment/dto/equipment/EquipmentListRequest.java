package com.backend.server.api.user.equipment.dto.equipment;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "장비 목록 조회 요청 DTO")
public class EquipmentListRequest extends AbstractPaginationParam {

    @Schema(description = "카테고리 ID로 정렬", example = "1")
    private Long categoryId;

    @Schema(description = "검색 키워드", example = "SONY")
    private String searchKeyword;

    @Schema(description = "검색 타입",
            example = "ALL", implementation = SearchType.class)
    private SearchType searchType = SearchType.ALL;

    @Schema(description = "장비 상태",
            example = "AVAILABLE", implementation = EquipmentStatus.class)
    private EquipmentStatus status = EquipmentStatus.ALL;

    @Schema(description = "정렬할 필드",
            implementation = SortBy.class, example = "id")
    private SortBy sortBy = SortBy.ID;

    public enum EquipmentStatus {
        ALL, AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING, RETURN_PENDING
    }

    public enum SearchType {
        ALL, MODEL_NAME, CATEGORY_NAME, RENTER_NAME
    }

    @Getter
    public enum SortBy implements SortTypeConvertible {
        NAME("name"),
        ID("id");

        private final String field;
        SortBy(String field) {
            this.field = field;
        }

        @Override
        public String getField() {
            return field;
        }
    }

    // 정렬 기준이 null일 경우를 대비한 안전한 toPageable()
    public SortBy getSortBy() {
        return sortBy == null ? SortBy.ID : sortBy;
    }

    public Pageable toPageable() {
        return super.toPageable(getSortBy());
    }
}
