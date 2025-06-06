package com.backend.server.api.admin.equipment.dto.equipment.response;

import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.enums.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentResponse {
    @Schema(description = "장비 ID", example = "123")
    private Long id;

    @Schema(description = "장비 모델명", example = "맥북 프로 16인치")
    private String modelName;

    @Schema(description = "장비 일련번호", example = "CAMCAN0001")
    private String serialNumber;

    @Schema(description = "장비 상태", example = "AVAILABLE")
    private Status status;

    @Schema(description = "파손 횟수", example = "2")
    private Long brokenCount;

    @Schema(description = "수리 횟수", example = "1")
    private Long repairCount;

    @Schema(description = "총 대여 횟수", example = "5")
    private Long rentalCount;

    @Schema(description = "현재 대여자 이름 (없으면 null)", example = "홍길동")
    private String renterName;

    @Schema(description = "장비 등록일", example = "2024-05-20T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "장비 사진", example = "https://어쩌구")
    private String imageUrl;

    @Schema(description = "관리자 ID", example = "42")
    private Long managerId;

    @Schema(description = "장비 카테고리명", example = "노트북")
    private String categoryName;

    @Schema(description = "장비 설명", example = "M2칩 탑재 모델, 16GB RAM")
    private String description;

    @Schema(description = "이용 제한 학년 (문자열)", example = "1234")
    private String restrictionGrade;

    @Schema(description = "대여 신청시각", example = "2024-05-20T10:00:00")
    private LocalDateTime requestedTime;

    @Schema(description = "대여 시작일", example = "2024-05-20T10:00:00")
    private LocalDateTime startRentTime;

    @Schema(description = "대여 종료일", example = "2024-05-27T10:00:00")
    private LocalDateTime endRentTime;

    public AdminEquipmentResponse(Equipment equipment) {
        if (equipment.getRenter() != null)
            this.renterName = equipment.getRenter().getName();
        else if (equipment.getSemesterSchedule() != null)
            this.renterName = equipment.getSemesterSchedule().getName();
        this.imageUrl = equipment.getImageUrl();
        this.id = equipment.getId();
        this.modelName = equipment.getEquipmentModel().getName();
        this.serialNumber = equipment.getSerialNumber();
        this.status = equipment.getStatus();
        this.brokenCount = equipment.getBrokenCount();
        this.repairCount = equipment.getRepairCount();
        this.rentalCount = equipment.getRentalCount();
        this.createdAt = equipment.getCreatedAt();
        this.imageUrl = equipment.getImageUrl();
        this.managerId = equipment.getManagerId();
        this.categoryName = equipment.getEquipmentCategory().getName();
        this.description = equipment.getDescription();
        this.restrictionGrade = equipment.getRestrictionGrade();
        this.requestedTime = equipment.getRequestedTime();
        this.startRentTime = equipment.getStartRentTime();
        this.endRentTime = equipment.getEndRentTime();
    }
}
