package com.backend.server.api.admin.equipment.dto.equipment.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExtendRentalPeriodsRequest {
    private List<Long> ids;
    private LocalDateTime newEndDate;
}