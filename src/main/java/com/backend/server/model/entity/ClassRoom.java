package com.backend.server.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

import com.backend.server.model.entity.enums.RentalStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 아이디

    @Column(nullable = false)
    private String name; // 강의실명

    @Column(nullable = false)
    private String imageUrl; // 강의실 이미지

    private String location; // 강의실 위치

    @Enumerated(EnumType.STRING)
    private RentalStatus status; // 강의실 상태 (RentalStatus 열거형 사용)

    private Long managerId; // 강의실 관리자 아이디

    private LocalDateTime availableStartTime; // 대여 가능 시작 시간

    private LocalDateTime availableEndTime; // 대여 가능 종료 시간

    private LocalDateTime rentalTime; // 강의실 대여 시간

    private LocalDateTime returnTime; // 강의실 반납 시간

    private Long renterId; // 대여자 아이디
}