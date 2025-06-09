package com.backend.server.api.common.dto;

import com.backend.server.model.entity.BoardCategory;

public record BoardResponse(
        Long id,
        String name,
        String description
) {
    public static BoardResponse from(BoardCategory category) {
        return new BoardResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
