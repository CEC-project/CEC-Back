package com.backend.server.api.admin.dto;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminEquipmentCreateRequest {
    @NotBlank(message = "장비 이름은 필수입니다")
    private String name;

    @NotBlank(message = "카테고리는 필수입니다")
    private String category;

    @NotBlank(message = "모델명은 필수입니다")
    private String modelName;

    @NotBlank(message = "상태는 필수입니다")
    private String status;

    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    private Integer quantity;

    private String description;
    
    private String attachment;
    
    // 관리자 ID (어드민 유저 중에서 선택)
    @NotNull(message = "관리자는 필수입니다")
    private Long managerId;
    
    // 관리자 이름 (표시용)
    private String managerName;
    
    // 대여 상태 (AVAILABLE, IN_USE, UNDER_REPAIR, RESERVED, LOST, DISPOSED)
    private String rentalStatus;
    
    // 대여 제한 학년 (체크박스로 다중 선택)
    private List<Integer> rentalRestrictedGrades;

    //업데이트에서도 쓸거라서 arguments에 existingEquipment도 넣음
    public Equipment toEntity(User manager, Equipment existingEquipment) {
        return Equipment.builder()
                .id(existingEquipment != null ? existingEquipment.getId() : null)
                .name(this.name)
                .category(this.category)
                .modelName(this.modelName)
                .status(this.status)
                .quantity(this.quantity)
                .description(this.description)
                .attachment(this.attachment)
                .managerId(this.managerId)
                .managerName(manager.getName())
                .rentalStatus(existingEquipment != null ? existingEquipment.getRentalStatus() : RentalStatus.AVAILABLE)
                .rentalTime(existingEquipment != null ? existingEquipment.getRentalTime() : null)
                .returnTime(existingEquipment != null ? existingEquipment.getReturnTime() : null)
                .renterId(existingEquipment != null ? existingEquipment.getRenterId() : null)
                .build();
    }
} 