package com.backend.server.api.admin.equipment.dto.equipment.response;

import lombok.Getter;

@Getter
public class AdminEquipmentIdResponse {
    private Long id;

    public AdminEquipmentIdResponse(Long id) {
        this.id = id;
    }
    
}
