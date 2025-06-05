package com.backend.server.api.admin.history.dto;

import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.enums.BrokenType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "수리/파손 이력 응답 DTO")
public class AdminBrokenRepairHistoryResponse {

    @Schema(description = "이력 ID", example = "12")
    private Long id;

    @Schema(description = "대상 타입 (EQUIPMENT, CLASSROOM)", example = "EQUIPMENT")
    private String targetType;

    @Schema(description = "이력 타입 (BROKEN, REPAIR)", example = "BROKEN")
    private String historyType;

    @Schema(description = "장비명 또는 강의실명", example = "SONY 카메라" /* 또는 "402호 강의실" */)
    private String name;

    @Schema(description = "장비 시리얼 번호 (강의실은 null)", example = "CAMSON250031", nullable = true)
    private String serialNumber;

    @Schema(description = "강의실 위치 (장비는 null)", example = "인관 3층 301호", nullable = true)
    private String location;

    @Schema(description = "파손/수리 상세 내용", example = "렌즈 파손 및 오작동 현상")
    private String detail;

    @Schema(description = "파손 유형(반납시 파손, 관리자 수동 처리) RETURN_BROKEN / ADMIN_BROKEN", nullable = true)
    private BrokenType brokenType;

    @Schema(description = "파손자 이름 (수리일 경우 null)", example = "홍길동", nullable = true)
    private String brokenByName;

    @Schema(description = "참조된 파손 이력 ID (수리 이력일 경우만)", example = "17", nullable = true)
    private Long brokenReferenceId;

    @Schema(description = "이력 생성 시각", example = "2024-06-01T15:30:00")
    private LocalDateTime createdAt;



    public AdminBrokenRepairHistoryResponse(BrokenRepairHistory history) {
        this.id = history.getId();
        this.targetType = history.getTargetType().name();
        this.historyType = history.getHistoryType().name();
        this.detail = history.getDetail();
        this.createdAt = history.getCreatedAt();
        this.brokenType = history.getBrokenType();
        this.brokenByName = history.getBrokenByName();
        if (history.getHistoryType() == BrokenRepairHistory.HistoryType.REPAIR &&
                history.getRelatedBrokenHistory() != null) {
            this.brokenReferenceId = history.getRelatedBrokenHistory().getId();
        }
        if (history.getTargetType() == BrokenRepairHistory.TargetType.EQUIPMENT && history.getEquipment() != null) {
            this.name = history.getEquipment().getEquipmentModel().getName();
            this.serialNumber = history.getEquipment().getSerialNumber();
            this.location = null;
        } else if (history.getTargetType() == BrokenRepairHistory.TargetType.CLASSROOM && history.getClassroom() != null) {
            this.name = history.getClassroom().getName();
            this.serialNumber = null;
            this.location = history.getClassroom().getLocation();
        } else {
            this.name = null;
            this.serialNumber = null;
            this.location = null;
        }
    }
}