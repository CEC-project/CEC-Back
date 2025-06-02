package com.backend.server.model.entity.classroom;

import com.backend.server.model.entity.BaseTimeEntity;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Classroom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User manager;

    private LocalTime startRentTime;
    private LocalTime endRentTime;

    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDateTime requestedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User renter;

    public void makeAvailable() {
        this.status = Status.AVAILABLE;
        this.startRentTime = null;
        this.endRentTime = null;
        this.requestedTime = null;
        this.renter = null;
    }

    public void makeRentalPending(LocalTime startRentTime, LocalTime endRentTime, User renter) {
        this.status = Status.RENTAL_PENDING;
        this.startRentTime = startRentTime;
        this.endRentTime = endRentTime;
        this.requestedTime = LocalDateTime.now();
        this.renter = renter;
    }

    public void makeInUse() {
        this.status = Status.IN_USE;
    }

    public void makeBroken(String detail) {
        makeAvailable();
        this.status = Status.BROKEN;

        // 파손 테이블에 저장
    }
}