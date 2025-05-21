package com.backend.server.api.user.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "내 정보 수정 요청 DTO")
public class MyInfoRequest {
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "휴대폰 번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "닉네임", example = "길동이")
    private String nickname;

    @Schema(description = "새 비밀번호")
    private String newPassword;

    @Schema(description = "새 비밀번호 확인")
    private String confirmPassword;

    @Schema(description = "프로필 이미지 S3 URL", example = "https://s3.bucket/path/profile.jpg")
    private String profilePicture;
}