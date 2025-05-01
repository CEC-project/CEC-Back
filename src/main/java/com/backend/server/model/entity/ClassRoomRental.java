package com.backend.server.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

import com.backend.server.model.entity.enums.RentalStatus;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "class_room_rentals")
public class ClassRoomRental extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_room_id", nullable = false)
    private Long classRoomId;

    @Column(name = "user_id", nullable = false)
    private Long renterId;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @Column
    private LocalDateTime rentalTime;

    @Column
    private LocalDateTime returnTime;


    
    // 반납 완료
    public void completeReturn() {
        this.status = RentalStatus.AVAILABLE;
    }

    // 반납 완료(피손)
    public void completeReturnDamaged() {
        this.status = RentalStatus.BROKEN;
    }
} 