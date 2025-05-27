package com.backend.server.api.user.equipment.dto.equipment;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EquipmentRentalRequest {
    private List<Long> equipmentIds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
