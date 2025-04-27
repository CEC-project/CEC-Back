package com.backend.server.api.admin.dto.classroom;

import com.backend.server.model.entity.enums.RentalStatus;
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
    private RentalStatus status; // 강의실 상태
    private String managerName; // 강의실 관리자
    private LocalDateTime availableStartTime; // 강의실 대여 가능 시작 시간
    private LocalDateTime availableEndTime; // 강의실 대여 가능 종료 시간
}