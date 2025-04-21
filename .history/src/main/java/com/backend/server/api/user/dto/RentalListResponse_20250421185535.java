package com.backend.server.api.user.dto;

import java.util.List;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.EquipmentRental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Page;
@Getter
@AllArgsConstructor
public class RentalListResponse {

    private List<RentalResponse> content;
    private PageableInfo pageable;
    private List<String> errorMessages;
    
    public RentalListResponse(Page<EquipmentRental> page) {
        this.content = page.getContent().stream().map(RentalResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
        this.errorMessages = null;
    }
    
    public RentalListResponse(List<RentalResponse> responses, List<String> errorMessages) {
        this.content = responses;
        this.pageable = null;
        this.errorMessages = errorMessages;
    }
}
