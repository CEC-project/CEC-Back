package com.backend.server.model.entity;

import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.equipment.Equipment;
import jakarta.persistence.Column;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 대여자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User renter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;
    public enum TargetType { EQUIPMENT, CLASSROOM }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Classroom classroom;

    private RentalHistoryStatus status;
    public enum RentalHistoryStatus {
        RENTAL_PENDING,     //승인대기
        IN_USE,             //사용중(승인됨)
        BROKEN,             //파손됨
        RETURN,             //반납됨
        CANCELLED,          //취소됨
        REJECTED,           //반려됨
        APPROVAL_CANCELLED  //관리자가 승인취소
        //정상적 흐름 : RENTAL_PENDING -> IN_USE -> RETURN
        //파손시 흐름 : RENTAL_PENDING -> IN_USE -> BROKEN -> RETURN
        //취소시 흐름 : RENTAL_PENDING -> CANCELLED
        //반려시 흐름 : RENTAL_PENDING -> REJECTED
        //관리자가 승인취소시 흐름 : RENTAL_PENDING -> IN_USE -> APPROVAL_CANCELLED
    }

    // 취소 사유
    private String reason;

    // 파손 내역
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BrokenRepairHistory brokenHistory;

    // 수리 내역
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BrokenRepairHistory repairHistory;

    // ---------------- 변경 메소드 --------------------- //

    public void makeInUse() {
        this.status = RentalHistoryStatus.IN_USE;
    }

    public void makeBroken(BrokenRepairHistory brokenHistory) {
        this.brokenHistory = brokenHistory;
        this.status = RentalHistoryStatus.BROKEN;
    }

    public void makeRepair(BrokenRepairHistory repairistory) {
        this.repairHistory = repairistory;
        this.status = RentalHistoryStatus.RETURN;
    }

    public void makeReturn() {
        this.status = RentalHistoryStatus.RETURN;
    }

    public void makeCancelled() {
        this.status = RentalHistoryStatus.CANCELLED;
    }

    public void makeRejected(String reason) {
        this.reason = reason;
        this.status = RentalHistoryStatus.REJECTED;
    }

    public void makeApprovalCancelled(String reason) {
        this.reason = reason;
        this.status = RentalHistoryStatus.APPROVAL_CANCELLED;
    }
}
