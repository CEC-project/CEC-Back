package com.backend.server.api.admin.equipment.dto.equipment.request;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import com.backend.server.model.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "장비 목록 조회 요청 DTO")
public class AdminEquipmentListRequest extends AbstractPaginationParam {

    @Schema(description = "카테고리 ID로 정렬")
    private Long categoryId;

    @Schema(description = "검색 키워드")
    private String searchKeyword;

    @Schema(description = "검색 타입",
            example = "ALL", implementation = SearchType.class)
    private SearchType searchType = SearchType.ALL;

    @Schema(description = "장비 상태",
            example = "AVAILABLE", implementation = EquipmentStatus.class)
    private EquipmentStatus status = EquipmentStatus.ALL;

    @Schema(description = "정렬 기준",
            example = "CREATED_AT", implementation = SortBy.class)
    private SortBy sortBy = SortBy.ID;

    public Pageable toPageable() {
        return super.toPageable(sortBy);
    }

    public enum SearchType {
        ALL, MODEL_NAME, CATEGORY_NAME, SERIAL_NUMBER, RENTER_NAME
    }

    public enum EquipmentStatus {
        ALL, AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING, RETURN_PENDING, CANCELABLE
    }

    @Getter
    public enum SortBy implements SortTypeConvertible {
        NAME("name"),
        ID("id"),
        CREATED_AT("createdAt"),
        RENTAL_COUNT("rentalCount"),
        REPAIR_COUNT("repairCount"),
        BROKEN_COUNT("brokenCount"),
        REQUESTED_AT("requestedAt");

        private final String field;
        SortBy(String field) {
            this.field = field;
        }

        @Override
        public String getField() {
            return field;
        }
    }
}
