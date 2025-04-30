package com.backend.server.model.entity;

import com.backend.server.model.entity.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_rentals")
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
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "rental_time", nullable = false)
    private LocalDateTime rentalTime;
    
    @Column(name = "return_time", nullable = false)
    private LocalDateTime returnTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "rental_status", nullable = false)
    private RentalStatus rentalStatus;
    
    
    public void returnEquipment(String returnCondition) {
        this.rentalStatus = RentalStatus.RETURN_PENDING;
    }

    public void rentalEquipment(String rentalCondition) {
        this.rentalStatus = RentalStatus.RENTAL_PENDING;
    }
    
    // 대여 요청 승인
    public void approveRental() {
        this.rentalStatus = RentalStatus.APPROVED;
    }
    
    // 대여 요청 거절
    public void rejectRental() {
        this.rentalStatus = RentalStatus.REJECTED;
    }
    
    // 반납 완료
    public void completeReturn() {
        this.rentalStatus = RentalStatus.AVAILABLE;
    }

    // 반납 완료(피손)
    public void completeReturnDamaged() {
        this.rentalStatus = RentalStatus.BROKEN;
    }
} 