package com.backend.server.api.user.inquiry.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Pageable;

/**
 * 사용자 자신의 문의글 목록 조회 파라미터
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "내 문의글 목록 조회 요청 DTO")
public class InquiryListRequest extends AbstractPaginationParam {

    @Schema(description = "정렬 기준", implementation = SortBy.class)
    @Builder.Default
    private SortBy sortBy = SortBy.CREATED_AT;

    public Pageable toPageable() {
        return super.toPageable(sortBy);
    }

    @Getter
    public enum SortBy implements SortTypeConvertible {
        ID("id"),
        CREATED_AT("createdAt");

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