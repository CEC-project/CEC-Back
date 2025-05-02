package com.backend.server.api.user.dto.equipment.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "장비 모델 목록 조회 요청")
@Getter
@Setter
@NoArgsConstructor
public class EquipmentModelListRequest {
    
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;
    
    @Schema(description = "검색어 (모델명, 영문코드 검색)", example = "카메라")
    private String keyword;
    
    @Schema(description = "사용 가능 여부", example = "true")
    private Boolean available;
    
    @Schema(description = "페이지 번호", example = "0")
    private Integer page = 0;
    
    @Schema(description = "페이지 크기", example = "10")
    private Integer size = 10;
    
    @Schema(description = "정렬 기준", example = "id")
    private String sortBy = "id";
    
    @Schema(description = "정렬 방향 (ASC, DESC)", example = "DESC")
    private String sortDirection = "DESC";
} 