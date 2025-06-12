package com.backend.server.api.user.classroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder(toBuilder = true)
public class ClassroomActionRequest {

    public enum Action {
        RENT_REQUEST,    // 대여 요청
        RENT_CANCEL,     // 대여 요청 취소
    }

    @Schema(description = """
            RENT_REQUEST: 대여 요청
            RENT_CANCEL: 대여 요청 취소""")
    private Action action;

    @Schema(description = "강의실 ID")
    private Long id;

    @Schema(description = "요청 시작일 (대여 요청 시에만 필요)", example = "10:00", implementation = String.class, nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime; //시:분 이므로 접두어로 At 말고 Time 사용

    @Schema(description = "요청 종료일 (대여 요청 시에만 필요)", example = "11:00", implementation = String.class, nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
}
