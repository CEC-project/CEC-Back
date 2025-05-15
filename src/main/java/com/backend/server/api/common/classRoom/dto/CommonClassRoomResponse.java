package com.backend.server.api.common.classRoom.dto;

import java.time.LocalDateTime;

import com.backend.server.model.entity.ClassRoom;
import com.backend.server.model.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class CommonClassRoomResponse {
    //아이디
    private Long id;
    //이름
    private String name;
    //위치
    private String location;
    //상태
    private Status status;
    //관리자
    private Long managerId;
    //대여가능시작시간
    private LocalDateTime availableStartTime;
    //대여가능종료시간
    private LocalDateTime availableEndTime;
    //대여종료시간
    private LocalDateTime returnTime;

    public CommonClassRoomResponse(ClassRoom classRoom) {
        this.id = classRoom.getId();
        this.name = classRoom.getName();
        this.location = classRoom.getLocation();
        this.status = classRoom.getStatus();
        this.managerId = classRoom.getManagerId();
        this.availableStartTime = classRoom.getAvailableStartTime();
        this.availableEndTime = classRoom.getAvailableEndTime();
        this.returnTime = classRoom.getReturnTime();
    }
    
}
