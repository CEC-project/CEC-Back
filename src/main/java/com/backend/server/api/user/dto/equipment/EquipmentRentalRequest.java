package com.backend.server.api.user.dto.equipment;

import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.enums.Status;
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
    private Integer quantity;

    public EquipmentRental toEntity(Long userId,Long equipmentId, LocalDateTime rentalTime, LocalDateTime returnTime, Status rentalStatus, Integer quantity) {
        return EquipmentRental.builder()
                .equipmentId(equipmentId)
                .userId(userId)
                .rentalTime(rentalTime)
                .returnTime(returnTime)
                .rentalStatus(rentalStatus)
                .quantity(quantity)
                .build();
    }
} 