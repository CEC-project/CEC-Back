package com.backend.server.api.user.equipment.dto.equipment;

import java.time.LocalDateTime;

import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentCartResponse {

    @Schema(description = "장비 id", example = "SONY")
    private Long id;

    @Schema(description = "장비 모델 이름", example = "SONY")
    private String modelName;

    @Schema(example = " AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING 중 하나")
    private Status status;

    @Schema(description = "장비 등록일", example = "2025-05-30T14:22:11")
    private LocalDateTime createdAt;

    @Schema(description = "장비 이미지 URL", example = "https://example.com/images/equipment.jpg")
    private String imageUrl;

    public EquipmentCartResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.modelName = equipment.getEquipmentModel().getName();
        this.status = Status.valueOf(equipment.getStatus().name());
        this.createdAt = equipment.getCreatedAt();
        this.imageUrl = equipment.getImageUrl();
    }
}
