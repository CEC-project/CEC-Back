package com.backend.server.api.admin.boardCategory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminBoardCategoryRequest {
    private String name;
    private String description;
}
