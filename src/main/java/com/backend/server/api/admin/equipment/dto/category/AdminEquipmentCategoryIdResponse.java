package com.backend.server.api.admin.equipment.dto.category;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "아이디 응답")
@Getter
public class AdminEquipmentCategoryIdResponse {
    private Long id;

    public AdminEquipmentCategoryIdResponse(Long id) {
        this.id = id;
    }
} 