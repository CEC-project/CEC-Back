package com.backend.server.api.admin.dto.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminCommonCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Long count;
    @JsonFormat(pattern = "yyyy-MM-dd HH시:mm분:ss초")
    private LocalDateTime createdAt;
}
