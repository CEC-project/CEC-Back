package com.backend.server.api.user.classroom.dto;

import com.backend.server.model.entity.ClassRoomRental;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomRentalResponse {
    private Long id;                  // 대여 ID
    private Long classRoomId;         // 장비 ID
    private LocalDateTime rentalTime; // 대여 시작 시간
    private LocalDateTime returnTime; // 반납 예정 시간
    private String status;            // 대여 상태 (PENDING, APPROVED)
    private LocalDateTime createdAt;  // 생성 시간

 
    
    public ClassRoomRentalResponse(ClassRoomRental rental) {
        this.id = rental.getId();
        this.classRoomId = rental.getClassRoomId();
        this.rentalTime = rental.getRentalTime();
        this.returnTime = rental.getReturnTime();
        this.status = rental.getStatus().toString();
        this.createdAt = rental.getCreatedAt();
    }
}
