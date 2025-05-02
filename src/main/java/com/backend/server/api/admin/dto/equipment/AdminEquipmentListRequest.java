package com.backend.server.api.admin.dto.equipment;

import com.backend.server.model.entity.enums.RentalStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장비 목록 조회 요청 DTO")
public class AdminEquipmentListRequest {
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "장비 대여상태나 상태", example = "AVAILABLE , IN_USE, BROKEN, RENTAL_PENDING, APPROVED, REJECTED, RETURN_PENDING")
    private RentalStatus rentalStatus;

    @Schema(description = "대여 가능 여부", example = "true")
    private Boolean available;

    @Schema(description = "검색어", example = "맥북")
    private String searchKeyword;

    @Schema(description = "검색 유형 (0: 사용자 이름, 1: 장비 이름, 2: 카테고리)", example = "1")
    private Integer searchType;

    @Schema(description = "페이지 번호", example = "1")
    private Integer page;

    @Schema(description = "페이지 크기", example = "17")
    private Integer size;

    @Schema(description = "정렬 기준 (name|category|status)", example = "name")
    private String sortBy;

    @Schema(description = "정렬 방향 (asc|desc)", example = "asc")
    private String sortDirection;

    // @Schema(description = "즐겨찾기한 장비만 필터링", example = "false")
 
    // private Boolean favoriteOnly;
    //관리자는 즐찾 기능이 없음
}