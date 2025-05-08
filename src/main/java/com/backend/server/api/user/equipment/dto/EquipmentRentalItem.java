package com.backend.server.api.user.equipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "장비 대여 항목 DTO")
public class EquipmentRentalItem {
    @Schema(description = "장비 ID", example = "1")
    private Long equipmentId;

    @Schema(description = "대여 수량", example = "5")
    private Integer quantity;
}