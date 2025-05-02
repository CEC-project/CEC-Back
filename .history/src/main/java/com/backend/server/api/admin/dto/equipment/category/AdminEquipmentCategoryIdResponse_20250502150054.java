package com.backend.server.api.admin.dto.equipment.category;

import com.backend.server.model.entity.EquipmentCategory;
import lombok.Getter;

@Getter
public class AdminEquipmentCategoryIdResponse {
    private Long id;

    public AdminEquipmentCategoryIdResponse(EquipmentCategory category) {
        this.id = category.getId();
    }
} 