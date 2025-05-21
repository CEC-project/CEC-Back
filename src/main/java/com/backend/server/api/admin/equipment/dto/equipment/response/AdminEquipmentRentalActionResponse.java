package com.backend.server.api.admin.equipment.dto.equipment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentRentalActionResponse {
    private Long equipmentId; // 처리된 장비 ID
    private String message;   // 처리 결과 메시지
} 