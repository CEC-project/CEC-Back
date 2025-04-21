package com.backend.server.api.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReturnListRequset {
    private LocalDateTime rentalTime; 
    private LocalDateTime returnTime; 
    private List<Long> equipmentIds;     // 대여할 장비 ID 목록
} 