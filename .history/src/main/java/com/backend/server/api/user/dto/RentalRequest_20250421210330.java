package com.backend.server.api.user.dto;

import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.enums.RentalStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RentalRequest {
    private Long equipmentId;         
    private LocalDateTime rentalTime; 
    private LocalDateTime returnTime; 
    
    public EquipmentRental toEntity(Long userId,Long equipmentId, LocalDateTime rentalTime, LocalDateTime returnTime) {
        return EquipmentRental.builder()
                .equipmentId(this.equipmentId)
                .userId(userId)
                .rentalTime(rentalTime)
                .returnTime(returnTime)
                .status(RentalStatus.PENDING)
                .build();
    }
} 