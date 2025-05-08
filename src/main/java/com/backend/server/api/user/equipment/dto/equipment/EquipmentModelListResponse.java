package com.backend.server.api.user.equipment.dto.equipment;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.EquipmentModel;

import lombok.Getter;

@Getter
public class EquipmentModelListResponse {
    private List<EquipmentModelResponse> content;
    private PageableInfo pageable;

    public EquipmentModelListResponse(Page<EquipmentModel> page) {
        this.content = page.getContent().stream().map(EquipmentModelResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
    public EquipmentModelListResponse(List<EquipmentModel> models) {
        this.content = models.stream()
                .map(EquipmentModelResponse::new)
                .toList();
        this.pageable = null; 
    }
    
}
