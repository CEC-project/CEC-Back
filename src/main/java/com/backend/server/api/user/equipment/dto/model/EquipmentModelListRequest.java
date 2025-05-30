package com.backend.server.api.user.equipment.dto.model;

import com.backend.server.api.common.dto.PageableRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springdoc.core.converters.models.Pageable;

@Schema(description = "장비 모델 목록 조회 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentModelListRequest implements PageableRequest {
    
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;
    
    @Schema(description = "검색어 (모델명, 영문코드 검색)", example = "카메라")
    private String keyword;
    
    // @Schema(description = "사용 가능 여부", example = "true")
    // private Boolean available;

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