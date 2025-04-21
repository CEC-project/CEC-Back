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
    private LocalDateTime actualReturnTime;
    private String returnCondition;
    
    public ReturnResponse(EquipmentRental rental) {
        this.id = rental.getId();
        this.equipmentId = rental.getEquipmentId();
        this.userId = rental.getUserId();
        this.status = rental.getStatus().name();
        this.rentalTime = rental.getRentalTime();
        this.returnTime = rental.getReturnTime();
        this.actualReturnTime = LocalDateTime.now(); // 실제 반납 시간은 현재 시간으로 설정
    }
    
    public static ReturnResponse fromEntity(EquipmentRental rental) {
        return ReturnResponse.builder()
                .id(rental.getId())
                .equipmentId(rental.getEquipmentId())
                .userId(rental.getUserId())
                .status(rental.getStatus().name())
                .rentalTime(rental.getRentalTime())
                .returnTime(rental.getReturnTime())
                .actualReturnTime(LocalDateTime.now())
                .build();
    }
} 