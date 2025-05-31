package com.backend.server.api.admin.history.dto;

import com.backend.server.model.entity.enums.BrokenType;
import com.backend.server.model.entity.equipment.EquipmentBrokenHistory;
import com.backend.server.model.entity.equipment.EquipmentRepairHistory;

import java.time.LocalDateTime;

public class RepairBrokenHistoryResponse {
    private Long id;
    private String targetType; // EQUIPMENT or CLASSROOM
    private String historyType; // BROKEN or REPAIR
    private String category; // 장비 카테고리(장비분류), 장비일 ㄱㅇㅇ우만
    private String name; // 장비명(모델이름) or 강의실명
    private String serialNumber; // 장비일 경우만
    private BrokenType brokenType; // 파손일 경우만
    private String detail;
    private LocalDateTime createdAt;

    // 장비 파손
    public RepairBrokenHistoryResponse(EquipmentBrokenHistory history) {
        this.id = history.getId();
        this.targetType = "EQUIPMENT";
        this.historyType = "BROKEN";
        this.category = history.getEquipment().getEquipmentCategory().getName();
        this.name = history.getEquipment().getEquipmentModel().getName();
        this.serialNumber = history.getEquipment().getSerialNumber();
        this.brokenType = history.getBrokenType();
        this.detail = history.getBrokenDetail();
        this.createdAt = history.getCreatedAt();
    }

    // 장비 수리
    public RepairBrokenHistoryResponse(EquipmentRepairHistory history) {
        this.id = history.getId();
        this.targetType = "EQUIPMENT";
        this.historyType = "REPAIR";
        this.category = history.getEquipment().getEquipmentCategory().getName();
        this.name = history.getEquipment().getEquipmentModel().getName();
        this.serialNumber = history.getEquipment().getSerialNumber();
        this.detail = history.getRepairDetail();
        this.createdAt = history.getCreatedAt();
    }
}
