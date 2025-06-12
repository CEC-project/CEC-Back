package com.backend.server.api.common.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;

// 사용자는 작성자의 닉네임까지만 볼수 있으므로, AuthorResponse 에서 실명 필드를 제거하였습니다.
public record AuthorResponse(
        Long id,
        String nickname,
        String imageUrl,
        Role role
) {
    public static AuthorResponse from(User user) {
        return new AuthorResponse(
                user.getId(),
                user.getNickname(),
                user.getProfilePicture(),
                user.getRole()
        );
    }
}
