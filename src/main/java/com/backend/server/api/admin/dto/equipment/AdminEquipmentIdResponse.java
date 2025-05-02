package com.backend.server.api.admin.dto.equipment;

import lombok.Getter;

@Getter
public class AdminEquipmentIdResponse {
    private Long id;

    public AdminEquipmentIdResponse(Long id) {
        this.id = id;
    }
    
}
