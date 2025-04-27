package com.backend.server.api.admin.dto.category;

import com.backend.server.model.entity.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminCategoryCreateRequest {
    @NotBlank(message = "카테고리 이름은 필수입니다")
    private String name;

    public Category toEntity() {
        return Category.builder()
                .name(this.name)
                .build();
    }
    
}
