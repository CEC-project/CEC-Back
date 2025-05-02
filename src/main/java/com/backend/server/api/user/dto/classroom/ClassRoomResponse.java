package com.backend.server.api.user.dto.classroom;

import java.time.LocalDateTime;
import com.backend.server.model.entity.ClassRoom;
import com.backend.server.model.entity.enums.RentalStatus;
import lombok.Getter;

@Getter
public class ClassRoomResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String location;
    private RentalStatus status;
    private String managerName;
    private LocalDateTime availableStartTime;
    private LocalDateTime availableEndTime;
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;
    private Boolean isFavorite;

    public ClassRoomResponse(ClassRoom classRoom) {
        this.id = classRoom.getId();
        this.name = classRoom.getName();
        this.imageUrl = classRoom.getImageUrl();
        this.location = classRoom.getLocation();
        this.status = classRoom.getStatus();
        this.managerName = classRoom.getManagerName();
        this.availableStartTime = classRoom.getAvailableStartTime();
        this.availableEndTime = classRoom.getAvailableEndTime();
        this.rentalTime = classRoom.getRentalTime();
        this.returnTime = classRoom.getReturnTime();
        this.isFavorite = classRoom.getFavorite();
    }
} 