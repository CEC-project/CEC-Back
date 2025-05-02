package com.backend.server.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.backend.server.model.entity.enums.RentalStatus;

@Entity
@Table(name = "equipment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Equipment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String serialNumber;


    @Column(nullable = false)
    private Long equipmentModelId;

    @Column(nullable = false)
    private RentalStatus rentalStatus;

    private Boolean isAvailable;

    private Integer rentalCount;

    private Integer brokenCount;

    private Integer repairCount;

    private String currentRenter;

    private LocalDateTime rentalTime;

    private LocalDateTime returnTime;
    
}