package com.backend.server.api.admin.dto.equipment.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AdminEquipmentCategoryCreateRequest {
    @NotBlank(message = "카테고리 이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "영문 코드는 필수 입력 항목입니다.")
    private String englishCode;
}
