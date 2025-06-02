package com.backend.server.api.user.equipment.dto.model;

import com.backend.server.model.entity.equipment.EquipmentModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EquipmentModelResponse {

    @Schema(description = "장비 모델 ID", example = "1")
    private Long id;

    @Schema(description = "장비 모델 이름", example = "SONY")
    private String name;

    @Schema(description = "장비 모델의 영어 코드", example = "CAMERA")
    private String englishCode;

    @Schema(description = "현재 사용 가능한지 여부", example = "true")
    private boolean available;

    @Schema(description = "장비 모델이 속한 카테고리 ID", example = "3")
    private Long categoryId;

    public EquipmentModelResponse(EquipmentModel equipmentModel) {
        this.id = equipmentModel.getId();
        this.name = equipmentModel.getName();
        this.englishCode = equipmentModel.getEnglishCode();
        this.available = equipmentModel.isAvailable();
        this.categoryId = equipmentModel.getCategory().getId();
    }
}
