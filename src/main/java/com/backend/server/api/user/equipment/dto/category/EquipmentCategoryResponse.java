package com.backend.server.api.user.equipment.dto.category;

import com.backend.server.model.entity.equipment.EquipmentCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class EquipmentCategoryResponse {

    @Schema(description = "장비 카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "장비 카테고리 이름", example = "카메라")
    private String name;

    @Schema(description = "장비 카테고리 영어 코드", example = "CAMERA")
    private String englishCode;

    @Schema(description = "최대 대여 가능 개수", example = "2")
    private Integer maxRentalCount;

    public EquipmentCategoryResponse(EquipmentCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.englishCode = category.getEnglishCode();
        this.maxRentalCount = category.getMaxRentalCount();
    }
}
