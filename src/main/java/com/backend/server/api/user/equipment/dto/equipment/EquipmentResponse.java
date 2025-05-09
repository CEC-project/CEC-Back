package com.backend.server.api.user.equipment.dto.equipment;

import java.time.LocalDateTime;

import com.backend.server.model.entity.Equipment;

public class EquipmentResponse {
    private String modelName;
    private boolean isAvailable;
    private LocalDateTime startRentDate;
    private LocalDateTime endRentDate;
    private String renterName;
    private String status;
    private LocalDateTime createdAt;
    
    public EquipmentResponse(Equipment equipment, String modelName, String renterName, LocalDateTime startRentDate, LocalDateTime endRentDate) {
        this.modelName = modelName;
        this.isAvailable = equipment.isAvailable();
        this.startRentDate = startRentDate;
        this.endRentDate = endRentDate;
        this.renterName = renterName;
        this.status = equipment.getRentalStatus().name();
        this.createdAt = equipment.getCreatedAt();
    }
}
