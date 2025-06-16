package com.backend.server.api.user.classroom.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.classroom.dto.ClassroomActionRequest;
import com.backend.server.api.user.classroom.dto.ClassroomResponse;
import com.backend.server.api.user.classroom.dto.ScheduleResponse;
import com.backend.server.api.user.classroom.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1-4. 대여 신청 / 강의실", description = "수정 완료")
@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;

    @Operation(
            summary = "휴일 + 특강 + 수업 + 대여 시간표 조회",
            description = "**일정이 시작되는 시간순으로 정렬해서 조회됩니다.**")
    @GetMapping("/{id}/schedule")
    public ApiResponse<List<ScheduleResponse>> getSchedule(
            @PathVariable("id") Long classroomId,
            @Schema(implementation = LocalDate.class, description = "yyyy-MM-dd 형식. 조회할 주차의 아무 날짜.")
            @RequestParam LocalDate date
    ) {
        return ApiResponse.success("시간표 조회 성공", classroomService.getSchedules(date, classroomId));
    }

    @Operation(summary = "강의실 목록 조회")
    @GetMapping("")
    public ApiResponse<List<ClassroomResponse>> getAllClassroom() {
        return ApiResponse.success("강의실 목록 조회 성공", classroomService.getAllClassrooms());
    }

    @PatchMapping("/action")
    @Operation(summary = "강의실 상태 변경 요청 (대여/취소)", description = "시간은 대여 요청 시에만 필요")
    public ApiResponse<Void> handleEquipmentAction(
            @Valid
            @RequestBody ClassroomActionRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        classroomService.handleUserAction(loginUser, request);
        return ApiResponse.success("강의실 대여/취소 요청 완료", null);
    }
}
