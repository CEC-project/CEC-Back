package com.backend.server.api.admin.equipment.dto.model;

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
