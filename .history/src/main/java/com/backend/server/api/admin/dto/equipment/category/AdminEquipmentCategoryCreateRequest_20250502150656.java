package com.backend.server.api.admin.dto.equipment.category;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.backend.server.model.entity.EquipmentCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "장비 카테고리 생성 / 수정 요청")
@Getter
@NoArgsConstructor
public class AdminEquipmentCategoryCreateRequest {
    @Schema(description = "카테고리 이름", example = "카메라")
    @NotBlank(message = "카테고리 이름은 필수 입력 항목입니다.")
    private String name;

    @Schema(description = "영문 코드", example = "CAMERA")
    @NotBlank(message = "영문 코드는 필수 입력 항목입니다.")
    private String englishCode;

    public EquipmentCategory toEntity() {
        return EquipmentCategory.builder()
                .name(this.name)
                .englishCode(this.englishCode)
                .build();
    }
}
