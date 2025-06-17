package com.backend.server.api.admin.equipment.dto.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.backend.server.model.entity.equipment.EquipmentCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "장비 카테고리 생성 / 수정 요청")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AdminEquipmentCategoryCreateRequest {
    @Schema(description = "카테고리 이름", example = "카메라")
    @NotBlank(message = "카테고리 이름은 필수 입력 항목입니다.")
    private String name;

    @Schema(description = "영문 코드", example = "CAMERA")
    @NotBlank(message = "영문 코드는 필수 입력 항목입니다.")
    private String englishCode;

    @Schema(description = "인당 대여 제한 갯수", example = "10")
    @NotNull(message = "대여 제한 수는 필수입니다.")
    @Min(value = 0, message = "0 이상 입력하세요.")
    private Integer maxRentalCount;

    

    public EquipmentCategory toEntity() {
        return EquipmentCategory.builder()
                .name(this.name)
                .englishCode(this.englishCode)
                .maxRentalCount(this.maxRentalCount)
                .build();
    }
}
