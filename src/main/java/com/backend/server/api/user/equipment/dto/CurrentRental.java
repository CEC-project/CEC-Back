package com.backend.server.api.user.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentRental {
    private LocalDateTime rentalStartTime;
    private LocalDateTime rentalEndTime;
    private Long rentedByUserId;
} 