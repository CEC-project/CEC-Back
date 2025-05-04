package com.backend.server.api.admin.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCommonCategoryRequest {
    private String name;
    private String description;
}
