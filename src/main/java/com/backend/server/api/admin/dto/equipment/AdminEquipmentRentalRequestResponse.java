package com.backend.server.api.admin.dto.equipment;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class AdminEquipmentRentalRequestResponse {
    private Long id;
    private Long equipmentId;
    private String equipmentName;
    //private String equipmentImageUrl;
    //private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;
    private Status rentalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //생성자에 장비랑 유저 엔티티는 왜넣냐?
    // ->엔티티에 연관관계 설정이 없으니까 이걸 서비스에서 조립할거에요 근데 이거 맞나?
    public AdminEquipmentRentalRequestResponse(EquipmentRental rental, Equipment equipment, User user) {
        this.id = rental.getId();
        this.equipmentId = rental.getEquipmentId();
        this.equipmentName = equipment != null ? equipment.getName() : null;
        //this.equipmentImageUrl = equipment != null ? equipment.getImageUrl() : null;
        //this.userId = rental.getUserId();
        this.userName = user != null ? user.getName() : null;
        this.userEmail = user != null ? user.getEmail() : null;
        this.rentalTime = rental.getRentalTime();
        this.returnTime = rental.getReturnTime();
        this.rentalStatus = rental.getRentalStatus();
        this.createdAt = rental.getCreatedAt();
        this.updatedAt = rental.getUpdatedAt();
    }
}
