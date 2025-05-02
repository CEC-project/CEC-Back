package com.backend.server.api.user.dto.equipment;

import com.backend.server.model.entity.enums.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "장비 목록 조회 요청 DTO")
public class EquipmentListRequest {
    @Schema(description = "페이지 번호", example = "1")
    private Integer page;            // 페이지 번호 (기본값: 1)

    @Schema(description = "페이지 크기", example = "17")
    private Integer size;            // 페이지 크기 (기본값: 17)

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;         // 장비 분류 ID

    @Schema(description = "대여 상태", example = "AVAILABLE", allowableValues = {
        "AVAILABLE", "IN_USE", "BROKEN", "RENTAL_PENDING", "APPROVED", "REJECTED", "RETURN_PENDING 중 하나나"
    })
    private Status rentalStatus;         

    @Schema(description = "대여 가능 여부", example = "true")
    private Boolean available;       // 대여 가능 여부

    @Schema(description = "검색어", example = "맥북")
    private String searchKeyword;    // 검색어 (장비 이름, 모델명)

    @Schema(description = "검색 유형 (0: 사용자 이름, 1: 장비 이름, 2: 카테고리)", example = "1")
    private Integer searchType;     

    @Schema(description = "정렬 기준 (name|category|status)", example = "name")
    private String sortBy;           // 정렬 기준 (name|category|status)

    @Schema(description = "정렬 방향 (asc|desc)", example = "asc")
    private String sortDirection;    // 정렬 방향 (asc|desc)

    @Schema(description = "즐겨찾기한 장비만 필터링", example = "false")
    private Boolean favoriteOnly;    // 즐겨찾기한 장비만 필터링

    @Schema(description = "장바구니에 있는 장비만 필터링", example = "false")
    private Boolean inCart;          // 장바구니에 있는 장비만 필터링

    @Schema(description = "즐겨찾기 여부", example = "false")
    private Boolean isFavorite;      // 즐겨찾기 여부

    @Schema(description = "사용자 ID (장바구니 및 즐겨찾기 필터링용)", example = "1")
    private Long userId;             // 사용자 ID (장바구니 및 즐겨찾기 필터링용)
}