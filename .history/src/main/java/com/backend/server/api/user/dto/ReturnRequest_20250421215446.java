package com.backend.server.api.user.dto;

import java.time.LocalDateTime;

import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.enums.RentalStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReturnRequest {
    private Long rentalId;           // 대여 ID
    private Long equipmentId;        // 장비 ID
    private LocalDateTime rentalTime; 
    private LocalDateTime returnTime; 

    public EquipmentRental toEntity(Long userId, Long equipmentId, LocalDateTime rentalTime, LocalDateTime returnTime) {
        return EquipmentRental.builder()
                .userId(userId)
                .equipmentId(equipmentId)
                .rentalTime(rentalTime)
                .returnTime(returnTime)
                .build();
    }
} 