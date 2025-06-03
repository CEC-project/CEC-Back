package com.backend.server.api.admin.equipment.dto.equipment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentBrokenOrRepairRequest {
    public enum EquipmentStatusActionType {
        BROKEN, REPAIR
    }

    private List<Long> ids;
    private EquipmentStatusActionType status;
    private String detail;
}
