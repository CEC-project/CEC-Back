package com.backend.server.api.user.equipment.dto.equipment;

import lombok.Data;

import java.util.List;

@Data
public class EquipmentCartListRequest {
    private List<Long> ids;
}
