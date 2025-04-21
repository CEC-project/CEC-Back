package com.backend.server.api.user.dto;

import java.time.LocalDateTime;

import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.enums.RentalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResponse {
    private Long id;
    private Long equipmentId;
    private Long userId;
    private String status;
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;

    
    public ReturnResponse(EquipmentRental rental) {
        this.id = rental.getId();
        this.equipmentId = rental.getEquipmentId();
        this.userId = rental.getUserId();
        this.status = rental.getStatus().name();
        this.rentalTime = rental.getRentalTime();
        this.returnTime = LocalDateTime.now();
    }
    
    public static ReturnResponse fromEntity(EquipmentRental rental) {
        return ReturnResponse.builder()
                .id(rental.getId())
                .equipmentId(rental.getEquipmentId())
                .userId(rental.getUserId())
                .status(rental.getStatus().name())
                .rentalTime(rental.getRentalTime())
                .returnTime(LocalDateTime.now())
                .build();
    }
} 