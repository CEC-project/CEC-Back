package com.backend.server.api.admin.equipment.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminEquipmentCountByCategoryResponse {
    @Schema(description = "장비 카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "장비 카테고리 이름", example = "카메라")
    private String name;

    @Schema(description = "카테고리 영문코드", example = "CAM")
    private String englishCode;

    @Schema(description = "해당 카테고리의 전체 장비 수", example = "20")
    private Integer totalCount;

    @Schema(description = "대여 가능한 장비 수", example = "15")
    private Integer available;

    @Schema(description = "사용자당 최대 대여 가능 수량", example = "2")
    private Integer maxRentalCount;

    @Schema(description = "파손된 장비 수", example = "3")
    private Integer brokenCount;
}
