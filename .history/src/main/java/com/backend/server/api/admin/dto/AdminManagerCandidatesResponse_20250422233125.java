package com.backend.server.api.admin.dto;



import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminManagerCandidatesResponse {
    Long user_id;
    String name;
    String nickname;
    Role role;

    public AdminManagerCandidatesResponse(User user) {
        this.user_id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.role = Role.valueOf(user.getRole());
    }
}
