package com.backend.server.api.user.dto.equipment;

import java.util.List;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.EquipmentRental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Page;
@Getter
@AllArgsConstructor
public class EquipmentRentalListResponse {

    private List<EquipmentRentalResponse> content;
    private PageableInfo pageable;
    private List<FailedRentalInfo> failedRequests;
    
    public EquipmentRentalListResponse(Page<EquipmentRental> page) {
        this.content = page.getContent().stream().map(EquipmentRentalResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
        this.failedRequests = null;
    }
    
    public EquipmentRentalListResponse(List<EquipmentRentalResponse> responses) {
        this.content = responses;
        this.pageable = null;
        this.failedRequests = null;
    }
    
    public EquipmentRentalListResponse(List<EquipmentRentalResponse> responses, List<FailedRentalInfo> failedRequests) {
        this.content = responses;
        this.pageable = null;
        this.failedRequests = failedRequests;
    }
    
    @Getter
    @AllArgsConstructor
    public static class FailedRentalInfo {
        private Long equipmentId;
        private String reason;
    }
}
