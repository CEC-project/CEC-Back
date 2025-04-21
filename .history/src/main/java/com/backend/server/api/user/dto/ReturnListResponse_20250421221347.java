package com.backend.server.api.user.dto;

import java.util.List;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.EquipmentRental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class ReturnListResponse {

    private List<RentalResponse> content;
    private PageableInfo pageable;
    private List<FailedReturnInfo> failedRequests;
    
    public ReturnListResponse(Page<EquipmentRental> page) {
        this.content = page.getContent().stream().map(RentalResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
        this.failedRequests = null;
    }
    
    public ReturnListResponse(List<RentalResponse> responses) {
        this.content = responses;
        this.pageable = null;
        this.failedRequests = null;
    }
    
    public ReturnListResponse(List<RentalResponse> responses, List<FailedReturnInfo> failedRequests) {
        this.content = responses;
        this.pageable = null;
        this.failedRequests = failedRequests;
    }
    
    @Getter
    @AllArgsConstructor
    public static class FailedReturnInfo {
        private Long equipmentId;
        private String reason;
    }
} 