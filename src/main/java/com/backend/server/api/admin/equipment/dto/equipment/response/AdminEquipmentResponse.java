package com.backend.server.api.admin.equipment.dto.equipment.response;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentResponse {
    private Long id;
    private String modelName;
    private String serialNumber;
    private Status status;
    private boolean isAvailable;
    private Long brokenCount;
    private Long repairCount;
    private Long rentalCount;
    private String renterName;
    private LocalDateTime createdAt;
    
    public AdminEquipmentResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.modelName = equipment.getEquipmentModel().getName();
        this.serialNumber = equipment.getSerialNumber();
        this.status = equipment.getStatus();
        this.isAvailable = equipment.isAvailable();
        this.brokenCount = equipment.getBrokenCount();
        this.repairCount = equipment.getRepairCount();
        this.rentalCount = equipment.getRentalCount();
        this.renterName = equipment.getRenter() != null
                ? equipment.getRenter().getName()
                : null;
        this.createdAt = equipment.getCreatedAt();
    }
}
