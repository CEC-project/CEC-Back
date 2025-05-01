package com.backend.server.api.admin.dto.equipment;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "장비 생성 요청 DTO")
public class AdminEquipmentCreateRequest {
    @Schema(description = "장비 이름", example = "맥북 프로 M3")
    @NotBlank(message = "장비 이름은 필수입니다")
    private String name;

    @Schema(description = "이미지 URL", example = "images/macbook_pro.jpg")
    @NotBlank(message = "이미지 URL은 필수입니다")
    private String imageUrl;

    @Schema(description = "카테고리 ID", example = "1")
    @NotNull(message = "카테고리는 필수입니다")
    private Long categoryId;

    @Schema(description = "모델명", example = "MacBook Pro 16-inch M3")
    @NotBlank(message = "모델명은 필수입니다")
    private String modelName;

    @Schema(
        description = "대여 상태 (AVAILABLE: 대여 가능, IN_USE: 대여중, BROKEN: 파손 등)",
        example = "AVAILABLE",
        allowableValues = {
        "AVAILABLE", 
        "IN_USE", 
        "BROKEN", 
        "RENTAL_PENDING", 
        "APPROVED", 
        "REJECTED", 
        "RETURN_PENDING"
        }
    )
    @NotNull(message = "대여 상태는 필수입니다")
    private RentalStatus rentalStatus;

    @Schema(description = "수량", example = "5")
    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    private Integer quantity;

    @Schema(description = "최대 대여 수량", example = "5")
    private Integer maxRentalCount;

    @Schema(description = "장비 설명", example = "2024년 신규 구매된 맥북 프로")
    private String description;
    
    @Schema(description = "부속장비비", example = "hdmi 등등등...")
    private String attachment;
    
    @Schema(description = "관리자 ID", example = "1")
    @NotNull(message = "관리자는 필수입니다")
    private Long managerId;
    
    @Schema(description = "대여 제한 학년 목록", example = "[1, 2, 3]")
    private List<Integer> rentalRestrictedGrades;

    //업데이트에서도 쓸거라서 arguments에 existingEquipment도 넣음
    public Equipment toEntity(User manager, Equipment existingEquipment) {
        return Equipment.builder()
                .id(existingEquipment != null ? existingEquipment.getId() : null)
                .name(this.name)
                .image_url(this.imageUrl)
                .categoryId(this.categoryId)
                .modelName(this.modelName)
                .rentalStatus(this.rentalStatus)
                .quantity(this.quantity)
                .maxRentalCount(this.maxRentalCount)
                .description(this.description)
                .attachment(this.attachment)
                .managerId(this.managerId)
                .managerName(manager.getName())
                .rentalTime(existingEquipment != null ? existingEquipment.getRentalTime() : null)
                .returnTime(existingEquipment != null ? existingEquipment.getReturnTime() : null)
                .renterId(existingEquipment != null ? existingEquipment.getRenterId() : null)
                .build();
    }
} 