package com.backend.server.api.admin.dto.classroom;

import com.backend.server.model.entity.ClassRoom;
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
public class AdminClassRoomCreateRequest {
    private Long id; // 아이디
    private String name; // 강의실 이름
    private String imageUrl; // 강의실 이미지
    private String location; // 강의실 위치
    private Status status; // 강의실 상태
    private Long managerId;
    private String managerName; // 강의실 관리자
    private LocalDateTime availableStartTime; // 강의실 대여 가능 시작 시간
    private LocalDateTime availableEndTime; // 강의실 대여 가능 종료 시간

    public ClassRoom toEntity(User manager, ClassRoom classRoom) {
        return ClassRoom.builder()
                .name(this.name)
                .imageUrl(this.imageUrl)
                .location(this.location)
                .rentalStatus(this.status)
                .managerId(manager.getId())
                .managerName(manager.getName())
                .availableStartTime(this.availableStartTime)
                .availableEndTime(this.availableEndTime)
                .build();
    }
}