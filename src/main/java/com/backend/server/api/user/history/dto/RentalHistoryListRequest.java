package com.backend.server.api.user.history.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.RentalHistory.RentalHistoryStatus;
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
    @Schema(description = """
            RENTAL_PENDING,     //승인대기
            IN_USE,             //사용중(승인됨)
            BROKEN,             //파손됨
            RETURN,             //반납됨
            CANCELLED,          //취소됨
            REJECTED,           //반려됨
            APPROVAL_CANCELLED  //관리자가 승인취소""")
    private RentalHistoryStatus status;
    private RentalHistorySortBy sortBy;
    private String searchKeyword;
    private RentalHistorySearchType searchType;

    @Schema(description = "조회 시작일", example = "2024-01-01")
    private LocalDate startDate;

    @Schema(description = "조회 종료일", example = "2024-12-31")
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
