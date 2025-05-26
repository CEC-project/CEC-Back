package com.backend.server.api.equipment.dto.equipment;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class EquipmentRentalRequest {
    private List<Long> equipmentIds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
