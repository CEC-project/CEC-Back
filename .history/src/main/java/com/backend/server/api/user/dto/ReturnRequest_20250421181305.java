package com.backend.server.api.user.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReturnRequest {
    private Long rentalId;           // 대여 ID
    private LocalDateTime rentalTime; 
    private LocalDateTime returnTime; 
} 