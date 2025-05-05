package com.backend.server.api.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.enums.Status;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "장비 생성 요청 DTO")
public class AdminEquipmentCreateRequest {
    @Schema(description = "이미지 URL", example = "images/macbook_pro.jpg")
    private String imageUrl;

    private Long categoryId; //EquipmentCategory를 참조하기 위한 아이디디

    private Long modelId; //EquipmentModel을 참조하기 위한 아이디

    private Long quantity; //장비 생성시에 이만큼 반복해서 하나씩 추가함함

    private boolean available;

    private Long managerId;

    private String description;

    private String restrictionGrade;
    
    public Equipment toEntity(String serialNumber) {
        return Equipment.builder()
                .imageUrl(imageUrl)
                .categoryId(categoryId)
                .modelId(modelId)
                .available(true)
                .rentalStatus(Status.AVAILABLE)
                .rentalCount(0L)
                .brokenCount(0L)
                .repairCount(0L)
                .managerId(managerId)
                .serialNumber(Long.parseLong(serialNumber))
                .description(description)
                .restrictionGrade(restrictionGrade)
                .build();
    }
}
