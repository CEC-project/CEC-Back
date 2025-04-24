package com.backend.server.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRentalApprovalRequest {
    private String comment; // 승인 or 거절 코멘트 (선택)
} 