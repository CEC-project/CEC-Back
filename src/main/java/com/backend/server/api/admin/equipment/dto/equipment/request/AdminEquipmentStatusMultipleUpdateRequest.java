package com.backend.server.api.admin.equipment.dto.equipment.request;

import com.backend.server.model.entity.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminEquipmentStatusMultipleUpdateRequest {
    private List<Long> equipmentIds;
    private Status status;
}
