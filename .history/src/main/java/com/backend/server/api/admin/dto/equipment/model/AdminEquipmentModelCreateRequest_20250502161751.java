package com.backend.server.api.admin.dto.equipment.model;

import com.backend.server.model.entity.EquipmentModel;

import lombok.Getter;
@Getter
@NoArgsConstructor
public class AdminEquipmentModelCreateRequest {
    private String name;
    private String imageUrl;
    private boolean available;
    private Long categoryId;
    private Integer rentalRestrictedGrades;

    public EquipmentModel toEntity() {
        return EquipmentModel.builder()
                .name(name)
                .imageUrl(imageUrl)
                .available(available)
                .categoryId(categoryId)
                .rentalRestrictedGrades(rentalRestrictedGrades)
                .build();
    }
}