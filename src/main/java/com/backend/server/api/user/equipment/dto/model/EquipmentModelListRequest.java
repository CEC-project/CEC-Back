package com.backend.server.api.user.equipment.dto.model;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Pageable;

@Schema(description = "장비 모델 목록 조회 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentModelListRequest extends AbstractPaginationParam {

    @Schema(description = "장비 카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "검색어 (모델명 또는 영문코드에 대해 부분 일치 검색)", example = "카메라")
    private String searchKeyword;

    @Schema(

            implementation = SortBy.class
    )

    private SortBy sortBy = SortBy.ID;


    public Pageable toPageable() {
        return super.toPageable(sortBy);
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
}
