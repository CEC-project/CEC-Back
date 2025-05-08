package com.backend.server.api.admin.equipment.dto;

import java.time.LocalDateTime;

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
@Schema(description = "장비 대여/반납 요청 목록 조회 요청 DTO")
public class AdminEquipmentRentalRequestListRequest {
    @Schema(description = "대여 상태", example = "RENTAL_PENDING", 
        allowableValues = {"RENTAL_PENDING", "RETURN_PENDING"})
    private Status rentalStatus;
    
    @Schema(description = "검색어", example = "맥북")
    private String searchKeyword;
    
    @Schema(description = "검색 유형 (0: 사용자 이름, 1: 장비 이름)", example = "1")
    private Integer searchType;
    
    @Schema(description = "시작 날짜 (ISO 8601 형식)", example = "2024-04-30T10:27:34.500Z")
    private LocalDateTime startDate;
    
    @Schema(description = "종료 날짜 (ISO 8601 형식)", example = "2024-04-30T10:27:34.500Z")
    private LocalDateTime endDate;
    
    @Schema(description = "정렬 기준 (createdAt|rentalTime|returnTime)", example = "createdAt")
    private String sortBy;
    
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private Integer page;
    
    @Schema(description = "페이지 크기", example = "10")
    private Integer size;
}
