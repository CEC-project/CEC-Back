package com.backend.server.model.entity;

import com.backend.server.api.admin.dto.AdminCategoryCreateRequest;
import com.backend.server.api.common.dto.CommonCategoryResponse;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public CommonCategoryResponse toDto() {  // Response DTO로 변환
        return CommonCategoryResponse.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
} 