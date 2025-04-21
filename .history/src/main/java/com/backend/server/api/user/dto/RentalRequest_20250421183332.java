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
    
    /**
     * DTO를 EquipmentRental 엔티티로 변환합니다.
     * @param userId 사용자 ID
     * @return 변환된 EquipmentRental 엔티티
     */
    public EquipmentRental toEntity(Long userId) {
        return EquipmentRental.builder()
                .equipmentId(this.equipmentId)
                .userId(userId)
                .rentalTime(this.rentalTime != null ? this.rentalTime : LocalDateTime.now())
                .returnTime(this.returnTime != null ? this.returnTime : LocalDateTime.now().plusDays(1))
                .status(RentalStatus.PENDING)
                .build();
    }
} 