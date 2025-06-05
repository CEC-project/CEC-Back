package com.backend.server.model.entity;

import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.BrokenType;
import com.backend.server.model.entity.equipment.Equipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BrokenRepairHistory extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 장비 or 강의실 구분
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HistoryType historyType;

    // 대상 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    // 공통 정보
    private String detail;

    @Enumerated(EnumType.STRING)
    private BrokenType brokenType; // 파손일 때만

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broken_by_id")
    private User brokenBy; // 파손일 때만

    private String brokenByName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_broken_id")
    private BrokenRepairHistory relatedBrokenHistory;

    @PrePersist
    public void prePersist() {
        if (this.brokenBy != null && (this.brokenByName == null || this.brokenByName.isBlank())) {
            this.brokenByName = this.brokenBy.getName();
        }
    }

    public enum TargetType { EQUIPMENT, CLASSROOM }
    public enum HistoryType { BROKEN, REPAIR }

    public static BrokenRepairHistory markAsBrokenWhenEquipmentReturn(Equipment equipment, User user, String detail) {
        return BrokenRepairHistory.builder()
                .equipment(equipment)
                .brokenBy(user)
                .brokenByName(user.getName())
                .detail(detail)
                .brokenType(BrokenType.RETURN_BROKEN)
                .targetType(TargetType.EQUIPMENT)
                .historyType(HistoryType.BROKEN)
                .build();
    }

    public static BrokenRepairHistory markAsBrokenEquipmentByAdmin(Equipment equipment, User user, String detail) {
        return BrokenRepairHistory.builder()
                .equipment(equipment)
                .brokenBy(user)
                .brokenByName(user.getName())
                .detail(detail)
                .brokenType(BrokenType.ADMIN_BROKEN)
                .targetType(TargetType.EQUIPMENT)
                .historyType(HistoryType.BROKEN)
                .build();
    }

    public static BrokenRepairHistory markAsRepairEquipment(Equipment equipment, User user, String detail, BrokenRepairHistory brokenRef) {
        return BrokenRepairHistory.builder()
                .equipment(equipment)
                .detail(detail)
                .targetType(TargetType.EQUIPMENT)
                .historyType(HistoryType.REPAIR)
                .brokenBy(user)  // 수리자 정보 넣을 수도 있음
                .brokenByName(user.getName())
                .relatedBrokenHistory(brokenRef) // 연관된 고장 이력
                .build();
    }

    public static BrokenRepairHistory markAsBrokenWhenClassroomReturn(Classroom classroom, User user, String detail) {
        return BrokenRepairHistory.builder()
                .classroom(classroom)
                .brokenBy(user)
                .brokenByName(user.getName())
                .detail(detail)
                .brokenType(BrokenType.RETURN_BROKEN)
                .targetType(TargetType.CLASSROOM)
                .historyType(HistoryType.BROKEN)
                .build();
    }

    public static BrokenRepairHistory markAsBrokenClassroomByAdmin(Classroom classroom, User admin, String detail) {
        return BrokenRepairHistory.builder()
                .classroom(classroom)
                .brokenBy(admin)
                .brokenByName(admin.getName())
                .detail(detail)
                .brokenType(BrokenType.ADMIN_BROKEN)
                .targetType(TargetType.CLASSROOM)
                .historyType(HistoryType.BROKEN)
                .build();
    }

    public static BrokenRepairHistory markAsRepairClassroom(Classroom classroom, User user, String detail, BrokenRepairHistory brokenRef) {
        return BrokenRepairHistory.builder()
                .classroom(classroom)
                .brokenBy(user)  // 수리 기록한 사람 정보
                .brokenByName(user.getName())
                .detail(detail)
                .targetType(TargetType.CLASSROOM)
                .historyType(HistoryType.REPAIR)
                .relatedBrokenHistory(brokenRef) // 연관된 고장 이력
                .build();
    }
}
