package com.backend.server.api.admin.history.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "수리/파손 이력 조회 요청")
public class AdminBrokenRepairHistoryRequest extends AbstractPaginationParam {

    @Schema(description = "이력 타입", example = "ALL", implementation = HistoryType.class)
    private HistoryType historyType = HistoryType.ALL;

    @Schema(description = "대상 타입", example = "EQUIPMENT", implementation = TargetType.class)
    private TargetType targetType = TargetType.ALL;

    @Schema(description = "검색 타입", example = "SONY")
    private SearchType searchType;

    @Schema(description = "검색 키워드 (모델명, 일련번호, 대여자 이름 포함)", example = "SONY")
    private String searchKeyword;

    @Schema(description = "조회 시작일", example = "2024-01-01")
    private LocalDate startDate;

    @Schema(description = "조회 종료일", example = "2024-12-31")
    private LocalDate endDate;

    @Schema(description = "정렬 기준", implementation = SortBy.class)
    private SortBy sortBy = SortBy.CREATED_AT;
    public Pageable toPageable() {
        return super.toPageable(sortBy);
    }

    public SortBy getSortBy() {
        return sortBy != null ? sortBy : SortBy.CREATED_AT;
    }

    public enum HistoryType { ALL, BROKEN, REPAIR }

    @Getter
    public enum SortBy implements SortTypeConvertible {
        CREATED_AT("createdAt");

        private final String field;
        SortBy(String field) { this.field = field; }
        public String getField() { return field; }
    }

    public enum TargetType{
        ALL, EQUIPMENT, CLASSROOM
    }

    public enum SearchType {
        ALL,
        MODEL_NAME,
        SERIAL_NUMBER,
        BROKEN_BY_NAME,
        LOCATION
    }
}
