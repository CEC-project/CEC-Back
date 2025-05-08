package com.backend.server.api.admin.classRoom.dto;

import java.time.LocalDateTime;

import com.backend.server.model.entity.ClassRoom;
import com.backend.server.model.entity.ClassRoomRental;
import com.backend.server.model.entity.User;

public class AdminClassRoomRentalRequestResponse {
    private Long id;
    private Long classRoomId;
    private String classRoomName;
    //private String equipmentImageUrl;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //생성자에 장비랑 유저 엔티티는 왜넣냐?
    // ->엔티티에 연관관계 설정이 없으니까 이걸 서비스에서 조립할거에요 근데 이거 맞나?
    public AdminClassRoomRentalRequestResponse(ClassRoomRental rental, ClassRoom classRoom, User user) {
        this.id = rental.getId();
        this.classRoomId = rental.getClassRoomId();
        this.classRoomName = classRoom != null ? classRoom.getName() : null;
        //this.equipmentImageUrl = equipment != null ? equipment.getImageUrl() : null;
        this.userId = rental.getRenterId();
        this.userName = user != null ? user.getName() : null;
        this.userEmail = user != null ? user.getEmail() : null;
        this.rentalTime = rental.getRentalTime();
        this.returnTime = rental.getReturnTime();
        this.status = rental.getStatus().toString();
        this.createdAt = rental.getCreatedAt();
        this.updatedAt = rental.getUpdatedAt();
    }
}
