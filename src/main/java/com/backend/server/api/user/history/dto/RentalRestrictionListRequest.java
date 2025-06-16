package com.backend.server.api.user.history.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.enums.RestrictionType;
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
public class RentalRestrictionListRequest extends AbstractPaginationParam {
    private RestrictionType targetType;
    private SortBy sortBy;

    @Schema(description = "조회 시작일 (yyyy-MM-dd 포맷)")
    private LocalDate startDate;
    @Schema(description = "조회 종료일 (yyyy-MM-dd 포맷)")
    private LocalDate endDate;

    @Getter
    public enum SortBy {
        ID("id");
        private final String field;
        SortBy(String field) {this.field = field;}
    }

    public Pageable toPageable() {
        if (sortBy == null)
            sortBy = SortBy.ID;
        return PageRequest.of(page, size, sortDirection, sortBy.field);
    }
}
