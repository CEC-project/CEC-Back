package com.backend.server.api.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "장비 생성 요청 DTO")
public class AdminEquipmentCreateRequest {
    @Schema(description = "이미지 URL", example = "images/macbook_pro.jpg")
    private String imageUrl;

    private Long categoryId;

    private Long modelId;

    private String modelName;

    private boolean available;
    
    
}
