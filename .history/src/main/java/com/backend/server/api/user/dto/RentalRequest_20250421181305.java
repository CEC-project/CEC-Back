package com.backend.server.api.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RentalRequest {
    private Long equipmentId;         
    private LocalDateTime rentalTime; 
    private LocalDateTime returnTime; 
 
} 