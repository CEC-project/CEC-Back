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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@Table(name = "classroom", indexes = {@Index(name = "idx_deleted_at_classroom", columnList = "deleted_at")})
@Builder(toBuilder = true)
public class Classroom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User manager;

    private LocalTime startRentTime;
    private LocalTime endRentTime;

    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User renter;

    public void makeAvailable() {
        this.status = Status.AVAILABLE;
        this.startRentTime = null;
        this.endRentTime = null;
        this.requestedAt = null;
        this.renter = null;
    }

    public void makeRentalPending(LocalTime startRentTime, LocalTime endRentTime, User renter) {
        this.status = Status.RENTAL_PENDING;
        this.startRentTime = startRentTime;
        this.endRentTime = endRentTime;
        this.requestedAt = LocalDateTime.now();
        this.renter = renter;
    }

    public void makeInUse() {
        this.status = Status.IN_USE;
    }

    public void makeBroken() {
        makeAvailable();
        this.status = Status.BROKEN;


    }
}