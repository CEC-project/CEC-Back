package com.backend.server.api.admin.dto.equipment.model;

import lombok.Getter;

@Getter
@NoArgsConstructor
public class AdminEquipmentModelCreateRequest {
    private Long equipmentCategoryId;

    private String imageUrl;
    private String category;
    private String brand;
    private String modelNumber;
}
