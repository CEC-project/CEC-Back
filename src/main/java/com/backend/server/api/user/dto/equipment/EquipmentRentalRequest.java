package com.backend.server.api.user.dto.equipment;

import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.enums.RentalStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentRentalRequest {
    private Long equipmentId;         
    private LocalDateTime rentalTime; 
    private LocalDateTime returnTime; 
    
    public EquipmentRental toEntity(Long userId,Long equipmentId, LocalDateTime rentalTime, LocalDateTime returnTime, RentalStatus status) {
        return EquipmentRental.builder()
                .equipmentId(this.equipmentId)
                .userId(userId)
                .rentalTime(rentalTime)
                .returnTime(returnTime)
                .status(status)
                .build();
    }
} 