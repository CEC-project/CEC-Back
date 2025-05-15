package com.backend.server.api.admin.equipment.dto;

import com.backend.server.model.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentStatusUpdateRequest {
    private Status status; // 변경할 장비 상태
    private String reason; // 상태 변경 이유 (선택 사항)
} 