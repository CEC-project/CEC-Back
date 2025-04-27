package com.backend.server.api.user.dto.equipment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RentalListRequest {
    private LocalDateTime startTime;     // 공통 대여 시작 시간 
    private LocalDateTime endTime;       // 공통 대여 종료 시간
    private List<Long> equipmentIds;     // 대여할 장비 ID 목록
} 