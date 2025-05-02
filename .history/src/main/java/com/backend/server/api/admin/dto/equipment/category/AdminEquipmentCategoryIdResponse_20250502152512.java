package com.backend.server.api.admin.dto.equipment.category;

import com.backend.server.model.entity.EquipmentCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "아이디 응답")
@Getter
public class AdminEquipmentCategoryIdResponse {
    private Long id;

    public AdminEquipmentCategoryIdResponse(EquipmentCategory category) {
        this.id = category.getId();
    }
} 