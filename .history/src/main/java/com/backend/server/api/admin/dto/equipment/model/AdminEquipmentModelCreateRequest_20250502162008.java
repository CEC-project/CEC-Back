package com.backend.server.api.admin.dto.equipment.model;

import com.backend.server.model.entity.EquipmentModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class AdminEquipmentModelCreateRequest {
    private String name;
    private String englishCode;
    private boolean available;
    private Long categoryId;

    public EquipmentModel toEntity() {
        return EquipmentModel.builder()
                .name(name)
                .englishCode(englishCode)
                .available(available)
                .categoryId(categoryId)
                .build();
    }
}