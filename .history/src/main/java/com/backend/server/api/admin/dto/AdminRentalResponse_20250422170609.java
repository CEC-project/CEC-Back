package com.backend.server.api.admin.dto;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRentalResponse {
    private Long id;                   // 대여 ID
    private Long equipmentId;          // 장비 ID
    private String equipmentName;      // 장비 이름
    private Long userId;               // 사용자 ID
    private String userName;           // 사용자 이름
    private String userEmail;          // 사용자 이메일
    private LocalDateTime rentalTime;  // 대여 시작 시간
    private LocalDateTime returnTime;  // 반납 예정 시간
    private String status;             // 대여 상태
    private LocalDateTime createdAt;   // 생성 시간
    private LocalDateTime updatedAt;   // 수정 시간
    
    public AdminRentalResponse(EquipmentRental rental, Equipment equipment, User user) {
        this.id = rental.getId();
        this.equipmentId = rental.getEquipmentId();
        this.equipmentName = equipment != null ? equipment.getName() : "Unknown Equipment";
        this.userId = rental.getUserId();
        this.userName = user != null ? user.getName() : "Unknown User";
        this.userEmail = user != null ? user.getEmail() : "Unknown Email";
        this.rentalTime = rental.getRentalTime();
        this.returnTime = rental.getReturnTime();
        this.status = rental.getStatus().toString();
        this.createdAt = rental.getCreatedAt();
        this.updatedAt = rental.getUpdatedAt();
    }
} 