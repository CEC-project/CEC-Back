package com.backend.server.api.user.equipment.dto.model;

import com.backend.server.api.common.dto.PageableRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springdoc.core.converters.models.Pageable;
import org.springdoc.core.converters.models.Sort;

@Schema(description = "장비 모델 목록 조회 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentModelListRequest implements PageableRequest {

    @Schema(description = "장비 카테고리 ID (선택)", example = "1")
    private Long categoryId;

    @Schema(description = "검색어 (모델명 또는 영문코드에 대해 부분 일치 검색)", example = "카메라")
    private String keyword;

    @Schema(description = "요청할 페이지 번호 (0부터 시작)", example = "0")
    private Integer page;

    @Schema(description = "페이지 당 항목 수", example = "20")
    private Integer size;

    @Getter
    public enum SortBy {
        NAME("name"),
        ID("id");

        private final String field;
        SortBy(String field) { this.field = field; }
        public String getField() { return field; }
    }

    public enum SortDirection {
        ASC, DESC
    }

    @Schema(
            description = """
        정렬할 필드 이름 (대문자로 전달해야 함)
        - NAME: 모델명 기준 정렬
        - ID: 모델 ID 기준 정렬
        """,
            example = "ID",
            implementation = SortBy.class
    )
    private SortBy sortBy = SortBy.ID;

    @Schema(
            description = """
        정렬 방향 (대문자로 전달해야 함)
        - ASC: 오름차순 정렬
        - DESC: 내림차순 정렬
        """,
            implementation = SortDirection.class,
            example = "DESC"
    )
    private SortDirection sortDirection = SortDirection.ASC;

    @Override public Integer getPage() { return page; }
    @Override public Integer getSize() { return size; }
    @Override public String getSortBy() { return sortBy.getField(); }
    @Override public String getSortDirection() { return String.valueOf(sortDirection); }
}
