package com.backend.server.api.admin.equipment.dto.equipment.request;

import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.enums.Status;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "장비 생성 요청 DTO")
public class AdminEquipmentCreateRequest {
    @Schema(description = "이미지 URL (장비 사진 경로)", example = "images/macbook_pro.jpg")
    private String imageUrl;

    @Schema(description = "장비 분류(카테고리) ID. EquipmentCategory의 PK", example = "1")
    private Long categoryId;

    @Schema(description = "장비 모델 ID. EquipmentModel의 PK", example = "1")
    private Long modelId;

    @Schema(description = "생성할 장비 개수. 입력한 수만큼 장비가 생성됨", example = "5")
    private Long quantity;

    @Schema(description = "담당 관리자 ID. User의 PK", example = "10")
    private Long managerId;

    @Schema(description = "장비 설명", example = "최신 맥북 프로 16인치")
    private String description;

    @Schema(description = "대여 제한 학년 (예: 3학년만 대여 가능)", example = "3")
    private String restrictionGrade;



    public Equipment toEntity(EquipmentCategory category, EquipmentModel model, String serialNumber) {
        return Equipment.builder()
                .imageUrl(imageUrl)
                .equipmentCategory(category)
                .equipmentModel(model)
                .serialNumber(serialNumber)  // serialNumber는 문자열로 저장
                .status(Status.AVAILABLE)
                .rentalCount(0L)
                .brokenCount(0L)
                .repairCount(0L)
                .managerId(managerId)
                .description(description)
                .restrictionGrade(restrictionGrade)
                .build();
    }


}
