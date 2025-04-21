package com.backend.server.model.entity;

import com.backend.server.model.entity.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentRental extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "rental_time", nullable = false)
    private LocalDateTime rentalTime;
    
    @Column(name = "return_time", nullable = false)
    private LocalDateTime returnTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;
    
    // 상태 변경 메서드
    public void approve() {
        this.status = RentalStatus.APPROVED;
    }
    
    public void reject() {
        this.status = RentalStatus.REJECTED;
    }
    
    public void returnEquipment(String returnCondition) {
        this.status = RentalStatus.RETURN_PENDING;
    }
} 