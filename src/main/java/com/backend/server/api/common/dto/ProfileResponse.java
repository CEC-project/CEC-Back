package com.backend.server.api.common.dto;

public record ProfileResponse(
        Long id,
        String name,
        String nickname,
        String profilePicture,
        String role
) {
}
