package com.backend.server.api.admin.equipment.dto;

import com.backend.server.model.entity.enums.Status;

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
    private String modelName;
    private String serialNumber;
    private String renterName;
    private Long categoryId;
    private String status;
    private Boolean isAvailable;
    private String sortBy; // id, createdAt, brokenCount, repairCount, rentalCount
    private String sortDirection; // asc, desc
    private String searchKeyword; // 통합 검색어
    private Integer page;
    private Integer size;
}