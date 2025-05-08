package com.backend.server.api.admin.equipment.dto.model;

import com.backend.server.model.entity.EquipmentModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class AdminEquipmentModelCreateRequest {
    @Schema(description = "장비 모델명", example = "캐논 EOS R6")
    private String name;

    @Schema(description = "장비 모델 영문 코드", example = "CANNON-EOS-R6")
    private String englishCode;

    @Schema(description = "장비 대여 가능 여부", example = "true")
    private boolean available;

    @Schema(description = "장비 카테고리 ID", example = "3")
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