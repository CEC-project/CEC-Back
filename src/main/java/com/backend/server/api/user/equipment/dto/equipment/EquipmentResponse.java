package com.backend.server.api.user.equipment.dto.equipment;

import java.time.LocalDateTime;

import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentResponse {

    @Schema(description = "장비 id", example = "SONY")
    private final Long id;

    @Schema(description = "장비 모델 이름", example = "SONY")
    private final String modelName;

    @Schema(description = "대여 시작 시간", example = "2025-06-01T09:00:00", nullable = true)
    private final LocalDateTime startRentAt;

    @Schema(description = "대여 종료 시간", example = "2025-06-03T18:00:00", nullable = true)
    private final LocalDateTime endRentAt;

    @Schema(description = "대여자 이름 또는 사용하는 수업 이름 (대여하지 않은 경우 null)", example = "홍길동", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String renterName;

    @Schema(example = " AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING 중 하나")
    private final Status status;

    @Schema(description = "장비 등록일", example = "2025-05-30T14:22:11")
    private final LocalDateTime createdAt;

    @Schema(description = "장비 이미지 URL", example = "https://example.com/images/equipment.jpg")
    private final String imageUrl;

    public EquipmentResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.modelName = equipment.getEquipmentModel().getName();
        this.startRentAt = equipment.getStartRentTime();
        this.endRentAt = equipment.getEndRentTime();
        this.renterName = equipment.getRenter() != null
                ? equipment.getRenter().getName()
                : null;
        this.status = Status.valueOf(equipment.getStatus().name());
        this.createdAt = equipment.getCreatedAt();
        this.imageUrl = equipment.getImageUrl();
    }


}
