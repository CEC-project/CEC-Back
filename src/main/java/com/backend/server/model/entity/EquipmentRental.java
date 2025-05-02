package com.backend.server.model.entity;

import com.backend.server.model.entity.enums.Status;
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
    private Status rentalStatus;
    
    
    public void returnEquipment(String returnCondition) {
        this.rentalStatus = Status.RETURN_PENDING;
    }

    public void rentalEquipment(String rentalCondition) {
        this.rentalStatus = Status.RENTAL_PENDING;
    }
    
    // 대여 요청 승인
    public void approveRental() {
        this.rentalStatus = Status.IN_USE;
    }
    
    // // 대여 요청 거절
    // public void rejectRental() {
    //     this.rentalStatus = RentalStatus.REJECTED;
    // }
    
    // 반납 완료, 대여 요청 거절
    public void completeReturn() {
        this.rentalStatus = Status.AVAILABLE;
    }

    // 반납 완료(피손)
    public void completeReturnDamaged() {
        this.rentalStatus = Status.BROKEN;
    }
} 