package com.backend.server.api.common.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;

public record AuthorResponse(
        Long id,
        String name,
        String nickname,
        String imageUrl,
        Role role
) {
    public static AuthorResponse from(User user) {
        return new AuthorResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getProfilePicture(),
                user.getRole()
        );
    }
}
