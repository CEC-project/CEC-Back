package com.backend.server.api.user.equipment.dto.category;

import com.backend.server.model.entity.EquipmentCategory;
import lombok.Getter;

@Getter
public class EquipmentCategoryResponse {
    private Long id;
    private String name;
    private String englishCode;
    private Integer maxRentalCount;
    public EquipmentCategoryResponse(EquipmentCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.englishCode = category.getEnglishCode();
        this.maxRentalCount = category.getMaxRentalCount();
    }
}