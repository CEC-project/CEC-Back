package com.backend.server.api.admin.equipment.dto.equipment.request;


import lombok.Data;

import java.util.List;

@Data
public class RepairEquipmentsRequest {
    private List<Long> equipmentIds;
    private String description; // nullable
}
