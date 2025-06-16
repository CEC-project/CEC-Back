package com.backend.server.api.user.history.dto;

import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryResponse;
import com.backend.server.api.user.classroom.dto.ClassroomResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.model.entity.RentalHistory;
import com.backend.server.model.entity.RentalHistory.RentalHistoryStatus;
import com.backend.server.model.entity.RentalHistory.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RentalHistoryResponse {
    private final Long id;

    @Schema(description = "EQUIPMENT, CLASSROOM 둘중 하나")
    private final TargetType targetType;

    @Schema(description = "targetType = EQUIPMENT 이면 null")
    private final ClassroomResponse classroom;

    @Schema(description = "targetType = CLASSROOM 이면 null")
    private final EquipmentResponse equipment;

    @Schema(description = "RENTAL_PENDING : 승인대기 || IN_USE : 사용중(승인됨) || BROKEN : 파손됨 || RETURN : 반납됨(수리됨)"
            + " || CANCELLED : 취소됨 || REJECTED : 반려됨 || APPROVAL_CANCELLED : 관리자가 승인취소<br><br>"
            + "detail 필드는 APPROVAL_CANCELLED 또는 REJECTED 상태인 경우만 표시<br>"
            + "broken 필드는 한번이라도 BROKEN 상태인적이 있었다면 표시<br>"
            + "repair 필드는 BROKEN 상태에서 RETURN 상태로 바뀐 경우만 표시")
    private final RentalHistoryStatus status;

    @Schema(description = "APPROVAL_CANCELLED 또는 REJECTED 상태인 경우만 표시")
    private final String detail;

    private final AdminBrokenRepairHistoryResponse broken;

    private final AdminBrokenRepairHistoryResponse repair;

    public RentalHistoryResponse(
            RentalHistory rentalHistory,
            ClassroomResponse classroom,
            EquipmentResponse equipment,
            AdminBrokenRepairHistoryResponse broken,
            AdminBrokenRepairHistoryResponse repair) {
        this.id = rentalHistory.getId();
        this.targetType = rentalHistory.getTargetType();
        this.classroom = classroom;
        this.equipment = equipment;
        this.status = rentalHistory.getStatus();
        this.detail = rentalHistory.getReason();
        this.broken = broken;
        this.repair = repair;
    }
}
