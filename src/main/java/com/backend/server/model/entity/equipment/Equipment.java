package com.backend.server.model.entity.equipment;

import com.backend.server.model.entity.BaseTimeEntity;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.backend.server.model.entity.enums.Status;

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

    @ManyToOne
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EquipmentCategory equipmentCategory;

    @ManyToOne
    @JoinColumn(name = "model_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EquipmentModel equipmentModel;

    // @Column(nullable = false)
    // private Long quantity;

    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "restriction_grade")
    private String restrictionGrade;   //이건 1234 이런식으로 입력받을거임 굳이 리스트 할 피룡가?

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "rental_count", nullable = false)
    private Long rentalCount;

    @Column(name = "broken_count", nullable = false)
    private Long brokenCount;

    @Column(name = "repair_count", nullable = false)
    private Long repairCount;

    @ManyToOne
    @JoinColumn(name = "renter_user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User renter;

    @ManyToOne
    @JoinColumn(name = "renter_semester_schdule_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SemesterSchedule semesterSchedule;

    @Column
    private LocalDateTime requestedTime;

    @Column(name = "start_rent_date")
    private LocalDateTime startRentTime;

    @Column(name = "end_rent_date")
    private LocalDateTime endRentTime;

    public void makeAvailable() {
        this.status = Status.AVAILABLE;
        this.startRentTime = null;
        this.endRentTime = null;
        this.requestedTime = null;
        this.renter = null;
    }
    public void makeRentalPending(LocalDateTime startRentTime, LocalDateTime endRentTime, User renter) {
        this.status = Status.RENTAL_PENDING;
        this.startRentTime = startRentTime;
        this.endRentTime = endRentTime;
        this.requestedTime = LocalDateTime.now();
        this.renter = renter;
    }

    public void makeInUse() {
        this.status = Status.IN_USE;
    }

    public void makeBroken() {
        makeAvailable();
        this.status = Status.BROKEN;

        // 파손 테이블에 저장
    }
}
