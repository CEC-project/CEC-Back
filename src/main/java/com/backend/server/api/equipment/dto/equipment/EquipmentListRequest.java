package com.backend.server.api.equipment.dto.equipment;

import com.backend.server.api.common.dto.PageableRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장비 목록 조회 요청 DTO")
public class EquipmentListRequest implements PageableRequest {
    @Schema(description = "모델명으로 필터링", example = "SONY")
    private String modelName;

    @Schema(description = "대여자 이름으로 필터링", example = "철수")
    private String renterName;

    @Schema(description = "카테고리 ID로 필터링", example = "1")
    private Long categoryId;

    @Schema(description = "사용 가능 여부로 필터링", example = "true")
    private Boolean isAvailable;

    @Schema(description = "키워드 검색", example = "sony")
    private String searchKeyword;

    // 페이징 및 정렬
    @Schema(description = "요청할 페이지 번호 (0부터 시작)", example = "0")
    private Integer page;

    @Schema(description = "페이지 당 항목 수", example = "20")
    private Integer size;

    @Schema(description = "정렬할 필드", example = "id")
    private String sortBy;

    @Schema(description = "정렬 방향 (ASC 또는 DESC)", example = "DESC")
    private String sortDirection;

    // PageableRequest 구현
    @Override public Integer getPage() { return page; }
    @Override public Integer getSize() { return size; }
    @Override public String getSortBy() { return sortBy; }
    @Override public String getSortDirection() { return sortDirection; }
}
