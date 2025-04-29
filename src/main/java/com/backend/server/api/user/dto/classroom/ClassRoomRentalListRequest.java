package com.backend.server.api.user.dto.classroom;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomRentalListRequest {
    private LocalDateTime startTime;     // 공통 대여 시작 시간 
    private LocalDateTime endTime;       // 공통 대여 종료 시간
    private List<Long> classRoomIds;     // 대여할 장비 ID 목록
} 