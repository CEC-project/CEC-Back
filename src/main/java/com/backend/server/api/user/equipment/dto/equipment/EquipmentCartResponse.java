package com.backend.server.api.user.equipment.dto.equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentCartResponse {
    private Long cartId;           // 장바구니 항목 ID
    private EquipmentResponse equipment;  // 장비 정보
}
