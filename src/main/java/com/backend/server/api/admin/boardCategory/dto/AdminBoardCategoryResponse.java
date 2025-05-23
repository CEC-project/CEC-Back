package com.backend.server.api.admin.boardCategory.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminBoardCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Long count;
    private LocalDateTime createdAt;
}
