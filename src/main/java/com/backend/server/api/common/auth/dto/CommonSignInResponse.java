package com.backend.server.api.common.auth.dto;

import com.backend.server.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonSignInResponse {
    private Long id;
    private String studentNumber;
    private String name;
    private String nickname;
    private String role;
    private String department;
    private String accessToken;
    private String profilePicture;
    @Builder.Default
    private String tokenType = "Bearer";

    public CommonSignInResponse(User user, String access) {
        this.id = user.getId();
        this.studentNumber = user.getStudentNumber();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
        this.department = user.getDepartment();
        this.accessToken = access;
        this.profilePicture = user.getProfilePicture();
        this.tokenType = "Bearer";
    }
}
