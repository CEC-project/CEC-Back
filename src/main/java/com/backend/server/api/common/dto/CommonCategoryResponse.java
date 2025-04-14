package com.backend.server.api.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonCategoryResponse {
    
    private Long id;
    
    @NotBlank(message = "카테고리 이름은 필수입니다")
    private String name;
    

} 