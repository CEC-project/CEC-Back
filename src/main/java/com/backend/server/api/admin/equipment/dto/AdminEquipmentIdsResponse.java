package com.backend.server.api.admin.equipment.dto;

import java.util.List;

import lombok.Getter;
@Getter
public class AdminEquipmentIdsResponse {
    private List<Long> ids;

    public AdminEquipmentIdsResponse(List<Long> ids) {
        this.ids = ids;
    }
    
    
    
}
