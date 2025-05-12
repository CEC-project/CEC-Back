package com.backend.server.api.user.equipment.dto.model;

import com.backend.server.model.entity.EquipmentModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EquipmentModelResponse {
    private Long id;
    private String name;
    private String englishCode;
    private boolean available;
    private Long categoryId;

    public EquipmentModelResponse(EquipmentModel equipmentModel) {
        this.id = equipmentModel.getId();
        this.name = equipmentModel.getName();
        this.englishCode = equipmentModel.getEnglishCode();
        this.available = equipmentModel.isAvailable();
        this.categoryId = equipmentModel.getCategoryId();
    }
}
