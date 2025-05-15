package com.backend.server.api.admin.classroom.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AdminClassroomDetailRequest {
    @NotEmpty(message = "내용를 입력해주세요.")
    @Size(max = 100, message = "내용는 100자 이하여야 합니다.")
    private String detail;
}
