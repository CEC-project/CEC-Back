package com.backend.server.api.user.equipment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "장비 대여 요청 DTO")
public class EquipmentRentalListRequest {
    @Schema(description = "대여 시작 시간", example = "2024-03-20T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "대여 종료 시간", example = "2024-03-20T18:00:00")
    private LocalDateTime endTime;

    @Schema(description = "대여할 장비 목록")
    private List<EquipmentRentalItem> items;
}
