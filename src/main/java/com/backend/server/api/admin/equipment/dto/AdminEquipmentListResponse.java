package com.backend.server.api.admin.equipment.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Equipment;
import lombok.Getter;

@Getter
public class AdminEquipmentListResponse {
    private List<AdminEquipmentResponse> content;
    private PageableInfo pageable;
    
    public AdminEquipmentListResponse(List<AdminEquipmentResponse> content, Page<Equipment> page) {
        this.content = content;
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
