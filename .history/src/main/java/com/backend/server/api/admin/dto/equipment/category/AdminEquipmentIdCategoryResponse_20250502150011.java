package com.backend.server.api.admin.dto.equipment.category;

import com.backend.server.model.entity.EquipmentCategory;
import lombok.Getter;

@Getter
public class AdminEquipmentIdCategoryResponse {
    private Long id;

    public AdminEquipmentIdCategoryResponse(EquipmentCategory category) {
        this.id = category.getId();
    }
} 