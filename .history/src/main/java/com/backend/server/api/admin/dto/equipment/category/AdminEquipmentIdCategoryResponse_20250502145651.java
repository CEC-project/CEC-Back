package com.backend.server.api.admin.dto.equipment;

import com.backend.server.model.entity.EquipmentCategory;
import lombok.Getter;

@Getter
public class AdminEquipmentCategoryResponse {
    private Long id;
    private String name;
    private String englishCode;

    public AdminEquipmentCategoryResponse(EquipmentCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.englishCode = category.getEnglishCode();
    }
} 