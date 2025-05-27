package com.backend.server.api.admin.equipment.dto.equipment.request;

import com.backend.server.model.entity.enums.Status;
import com.backend.server.api.common.dto.PageableRequest;

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
public class AdminEquipmentListRequest implements PageableRequest {

    @Schema(description = "모델명 (부분 일치 검색)", example = "카메라")
    private String modelName;

    @Schema(description = "장비 일련번호 (부분 일치 검색)", example = "CAMCAN0001")
    private String serialNumber;

    @Schema(description = "현재 대여자 이름 (부분 일치 검색)", example = "홍길동")
    private String renterName;

    @Schema(description = "장비 카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "장비 상태 (AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING, RETURN_PENDING)/ 정상, 대여중, 파손, 대여요청중, 반납대기중", example = "AVAILABLE")
    private String status;

//    @Schema(description = "대여 가능 여부", example = "true")
//    private Boolean isAvailable;

    @Schema(description = "모델명, 일련번호, 대여자 이름에 대한 통합 키워드 검색", example = "SONY")
    private String searchKeyword;

    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private Integer page;

    @Schema(description = "페이지당 항목 수", example = "17")
    private Integer size;

    @Schema(description = "정렬 기준 (id, createdAt, rentalCount, repairCount, brokenCount 등)", example = "createdAt")
    private String sortBy;

    @Schema(description = "정렬 방향 (asc 또는 desc)", example = "desc")
    private String sortDirection;

    @Override public Integer getPage() { return page; }
    @Override public Integer getSize() { return size; }
    @Override public String getSortBy() { return sortBy; }
    @Override public String getSortDirection() { return sortDirection; }
}