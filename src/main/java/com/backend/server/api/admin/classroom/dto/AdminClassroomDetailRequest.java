package com.backend.server.api.admin.classroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminClassroomDetailRequest {
    public enum Status {
        RETURN, REJECT, CANCEL, BROKEN, ACCEPT, REPAIR
    }

    @NotNull
    @Schema(description = "필수")
    private List<Long> ids;

    @NotNull
    @Schema(description = "필수")
    private Status status;

    @Schema(description = "'신청 반려' or '승인 취소' or '파손' 설명 (생략 가능)")
    @Size(max = 100, message = "내용는 100자 이하여야 합니다.")
    private String detail;
}
