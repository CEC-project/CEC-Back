package com.backend.server.api.user.mypage.dto;

import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyInfoResponse {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "닉네임", example = "길동이")
    private String nickname;

    @Schema(description = "프로필 사진 URL", example = "https://example.com/profile.jpg", nullable = true)
    private String profilePicture;

    public MyInfoResponse(LoginUser user) {
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.nickname = user.getNickname();
        this.profilePicture = user.getProfilePicture();
    }
}
