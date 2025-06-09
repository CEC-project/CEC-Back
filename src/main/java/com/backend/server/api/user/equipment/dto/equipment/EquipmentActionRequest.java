package com.backend.server.api.user.equipment.dto.equipment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter

@Builder(toBuilder = true)
public class EquipmentActionRequest {

    public enum Action {
        RENT_REQUEST,    // 대여 요청
        RENT_CANCEL,     // 대여 요청 취소
        RETURN_REQUEST,  // 반납 요청
        RETURN_CANCEL    // 반납 요청 취소
    }


    @Schema(description = "RENT_REQUEST: 대여 요청\n" +
            "RENT_CANCEL: 대여 요청 취소\n" +
            "RETURN_REQUEST: 반납 요청\n" +
            "RETURN_CANCEL: 반납 요청 취소")
    private Action action;

    @Schema(description = "장비 ID 목록", example = "[1, 2, 3]")
    private List<Long> ids;

    @Schema(description = "요청 시작일 (대여 요청 시에만 필요)", example = "2025-06-03T10:00:00", nullable = true)
    private LocalDateTime startAt;

    @Schema(description = "요청 종료일 (대여 요청 시에만 필요)", example = "2025-06-05T18:00:00", nullable = true)
    private LocalDateTime endAt;
}
