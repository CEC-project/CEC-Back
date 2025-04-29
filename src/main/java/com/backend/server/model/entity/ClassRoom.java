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
    @Column(nullable = false)
    private String location; // 강의실 위치

    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus; // 강의실 상태 (RentalStatus 열거형 사용)
    @Column
    private Long managerId; // 강의실 관리자 아이디

    private String managerName;

    @Column
    private Boolean available; // 대여 가능 여부
    @Column
    private LocalDateTime availableStartTime; // 대여 가능 시작 시간
    @Column
    private LocalDateTime availableEndTime; // 대여 가능 종료 시간
    @Column
    private LocalDateTime rentalTime; // 강의실 대여 시간
    @Column
    private LocalDateTime returnTime; // 강의실 반납 시간
    @Column
    private Long renterId; // 대여자 아이디

    @Column
    private Boolean favorite; // 즐겨찾기 여부

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    public RentalStatus getStatus() {
        return status;
    }

    public void addFavorite() {
        this.favorite = true;
    }

    public void removeFavorite() {
        this.favorite = false;
    }

    // 대여 상태 업데이트를 위한 Setter 메소드
    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }
    
    
    // 대여 시간 업데이트를 위한 Setter 메소드
    public void setRentalTime(LocalDateTime rentalTime) {
        this.rentalTime = rentalTime;
    }
    
    // 반납 시간 업데이트를 위한 Setter 메소드
    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }
    
    // 대여 가능 여부 업데이트를 위한 Setter 메소드
    public void setAvailable(Boolean available) {
        this.available = available;
    }
}