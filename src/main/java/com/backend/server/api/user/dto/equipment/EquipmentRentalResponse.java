package com.backend.server.api.user.dto.equipment;

import com.backend.server.model.entity.EquipmentRental;
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
public class EquipmentRentalResponse {
    private Long id;                  // 대여 ID
    private Long equipmentId;         // 장비 ID
    private LocalDateTime rentalTime; // 대여 시작 시간
    private LocalDateTime returnTime; // 반납 예정 시간
    private Status rentalStatus;            // 대여 상태 (PENDING, APPROVED)
    private LocalDateTime createdAt;  // 생성 시간

 
    
    public EquipmentRentalResponse(EquipmentRental rental) {
        this.id = rental.getId();
        this.equipmentId = rental.getEquipmentId();
        this.rentalTime = rental.getRentalTime();
        this.returnTime = rental.getReturnTime();
        this.rentalStatus = rental.getRentalStatus();
        this.createdAt = rental.getCreatedAt();
    }
}
