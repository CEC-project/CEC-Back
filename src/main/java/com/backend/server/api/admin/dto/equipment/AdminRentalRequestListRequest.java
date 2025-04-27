package com.backend.server.api.admin.dto.equipment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRentalRequestListRequest {
    private String status;               // 대여 상태 필터링 (선택)
    private String searchKeyword;    // 검색어 (장비 이름, 모델명)
    private Integer searchType;      // 검색 유형 (0:username, 1:equipment_name, 2:category)
    private LocalDateTime startDate;     // 조회 시작 날짜 (선택)
    private LocalDateTime endDate;       // 조회 종료 날짜 (선택)
    private String sortBy;        // 정렬 기준 (선택) - ex: ["createdAt,desc", "status,asc"]
    private Integer page;                // 페이지 번호 (기본값 0)
    private Integer size;                // 페이지 크기 (기본값 10)
}
