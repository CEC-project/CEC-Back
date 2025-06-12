package com.backend.server.api.common.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;

// 관리자는 작성자의 실명까지 볼수 있으므로, AuthorResponse 에서 실명 필드를 추가하였습니다.
public record AdminAuthorResponse(
        Long id,
        String name,
        String nickname,
        String imageUrl,
        Role role
) {
    public static AdminAuthorResponse from(User user) {
        return new AdminAuthorResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getProfilePicture(),
                user.getRole()
        );
    }
}
