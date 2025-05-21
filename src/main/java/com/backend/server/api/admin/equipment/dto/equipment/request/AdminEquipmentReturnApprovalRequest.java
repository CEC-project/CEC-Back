package com.backend.server.api.admin.equipment.dto.equipment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentReturnApprovalRequest {
    private String condition; // 장비 상태 설명 (선택)
    private String comment;   // 반납 승인 코멘트 (선택)
} 