package com.backend.server.model.entity;

import com.backend.server.model.entity.classroom.SemesterSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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

    @Column(nullable = false)
    private boolean available;

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

    @Column(name = "start_rent_date")
    private LocalDateTime startRentDate;

    @Column(name = "end_rent_date")
    private LocalDateTime endRentDate;
}
