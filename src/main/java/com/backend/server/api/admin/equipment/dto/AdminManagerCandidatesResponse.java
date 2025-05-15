package com.backend.server.api.admin.equipment.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "관리자 후보자 정보 응답 DTO")
public class AdminManagerCandidatesResponse {
    @Schema(description = "사용자 ID", example = "1")
    Long user_id;
    
    @Schema(description = "사용자 이름", example = "홍길동")
    String name;
    
    @Schema(description = "사용자 닉네임", example = "hong123")
    String nickname;
    
    @Schema(description = "사용자 권한", example = "USER")
    Role role;

    public AdminManagerCandidatesResponse(User user) {
        this.user_id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}
