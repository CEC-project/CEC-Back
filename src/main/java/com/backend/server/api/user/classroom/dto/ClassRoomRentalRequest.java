package com.backend.server.api.user.classroom.dto;

import com.backend.server.model.entity.ClassRoomRental;
import com.backend.server.model.entity.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ClassRoomRentalRequest {
    private Long classRoomId;         
    private LocalDateTime rentalTime; 
    private LocalDateTime returnTime; 
    
    public ClassRoomRental toEntity(Long userId,Long equipmentId, LocalDateTime rentalTime, LocalDateTime returnTime, Status status) {
        return ClassRoomRental.builder()
                .classRoomId(this.classRoomId)
                .renterId(userId)
                .rentalTime(rentalTime)
                .returnTime(returnTime)
                .status(status)
                .build();
    }
} 