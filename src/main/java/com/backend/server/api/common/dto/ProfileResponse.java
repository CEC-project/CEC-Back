package com.backend.server.api.common.dto;

import com.backend.server.model.entity.User;

public record ProfileResponse(
        Long id,
        String name,
        String nickname,
        String profilePicture,
        String role
) {
    public static ProfileResponse from(User user) {
        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getProfilePicture(),
                user.getRole().name()
        );
    }
}
