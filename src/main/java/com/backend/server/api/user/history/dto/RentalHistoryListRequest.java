package com.backend.server.api.user.history.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.RentalHistory.RentalHistoryStatus;
import com.backend.server.model.entity.RentalHistory.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalHistoryListRequest extends AbstractPaginationParam {
    private RentalHistoryStatus status;
    private TargetType targetType;
    private RentalHistorySortBy sortBy;
    private String searchKeyword;
    private RentalHistorySearchType searchType;

    @Schema(description = "조회 시작일 (yyyy-MM-dd 포맷)")
    private LocalDate startDate;
    @Schema(description = "조회 종료일 (yyyy-MM-dd 포맷)")
    private LocalDate endDate;

    public enum RentalHistorySearchType {
        ID,
        CLASSROOM_NAME,
        EQUIPMENT_MODEL_NAME,
        EQUIPMENT_CATEGORY_NAME,
        EQUIPMENT_SERIAL_NUMBER,
        ALL
    }

    @Getter
    public enum RentalHistorySortBy {
        ID("id");
        private final String field;
        RentalHistorySortBy(String field) {this.field = field;}
    }

    public Pageable toPageable() {
        if (sortBy == null)
            sortBy = RentalHistorySortBy.ID;
        return PageRequest.of(page, size, sortDirection, sortBy.field);
    }
}
