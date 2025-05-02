package com.backend.server.api.admin.dto.equipment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminEquipmentModelIdResponse {
    private Long id;

    public AdminEquipmentModelIdResponse(Long id) {
        this.id = id;
    }
}
