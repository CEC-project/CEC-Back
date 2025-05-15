package com.backend.server.api.admin.equipment.dto;

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
    private String modelName;
    private String serialNumber;
    private String renterName;
    private Long categoryId;
    private String status;
    private Boolean isAvailable;
    private String searchKeyword;

    //
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
    // Getter
    @Override public Integer getPage() { return page; }
    @Override public Integer getSize() { return size; }
    @Override public String getSortBy() { return sortBy; }
    @Override public String getSortDirection() { return sortDirection; }
}