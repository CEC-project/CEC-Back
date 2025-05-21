package com.backend.server.api.admin.equipment.dto.equipment.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminEquipmentRentalRequest {
    private Long equipmentId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
} 