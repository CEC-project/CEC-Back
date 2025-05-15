package com.backend.server.api.common.dto;

import java.time.LocalDateTime;
import com.backend.server.model.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카테고리 정보 응답 DTO")
public class CommonCategoryResponse {
    
    @Schema(description = "카테고리 ID", example = "1")
    private Long id;
    
    @Schema(description = "카테고리 이름", example = "카메라")
    private String name;
    
    @Schema(description = "생성일시", example = "2024-03-20T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일시", example = "2024-03-20T10:00:00")
    private LocalDateTime updatedAt;

    public CommonCategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
    

} 