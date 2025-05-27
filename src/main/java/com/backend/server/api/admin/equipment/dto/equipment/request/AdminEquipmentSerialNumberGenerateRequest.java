package com.backend.server.api.admin.equipment.dto.equipment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "장비 시리얼넘버 미리보기 요청")
public class AdminEquipmentSerialNumberGenerateRequest {

    @Schema(description = "장비 분류(카테고리) ID. EquipmentCategory의 PK", example = "1")
    private Long categoryId;

    @Schema(description = "장비 모델 ID. EquipmentModel의 PK", example = "1")
    private Long modelId;

}
