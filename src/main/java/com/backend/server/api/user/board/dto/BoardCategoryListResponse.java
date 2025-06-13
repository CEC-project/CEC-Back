package com.backend.server.api.user.board.dto;

import lombok.Data;

@Data
public class BoardCategoryListResponse {
    private Long id;

    private String name;

    private String description;
}
