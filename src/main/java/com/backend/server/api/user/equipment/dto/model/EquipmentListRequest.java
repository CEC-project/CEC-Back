package com.backend.server.api.user.equipment.dto.model;

public class EquipmentListRequest {
    private String modelName;
    private String renterName;
    private Long categoryId;
    private Boolean isAvailable;
    private String sortBy; // id, createdAt, brokenCount, repairCount, rentalCount
    private String sortDirection; // asc, desc
    private String searchKeyword; // 통합 검색어
    private Integer page;
    private Integer size;

}
