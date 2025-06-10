package com.backend.server.api.user.classroom.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ScheduleResponse {
    public enum ScheduleType {
        HOLIDAY, SPECIAL_LECTURE, LECTURE, RENTAL
    }

    @Schema(description = "HOLIDAY(휴일), SPECIAL_LECTURE(특강), LECTURE(수업), RENTAL(사용자가 강의실 대여) 중 하나.")
    private final ScheduleType type;

    @Schema(description = "요일을 나타냄. 1, 2, 3, 4, 5 중 하나.")
    private final Integer day;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "13:00", implementation = String.class, description = "일정 시작 시간")
    private final LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "13:00", implementation = String.class, description = "일정 종료 시간")
    private final LocalTime endTime;

    private final String color;

    @Schema(description = """
            type = HOLIDAY / SPECIAL_LECTURE / LECTURE 이면 일정 이름을 나타냅니다.<br>
            type = RENTAL 이면, 이 값은 항상 상태 Enum 값입니다.""")
    private final String content;

    @Schema(description = "강의실 id / 일정 id / 수업 id 중 하나.")
    private final Long id;

    @Schema(description = """
            type = RENTAL 이면서, 현재 로그인된 사람이 강의실 대여자일때만 true(현재 로그인한 사용자가 대여 취소가능하면 true)""")
    @JsonProperty("isRenter")
    private final boolean isRenter;

    @Schema(description = "대여자의 이름 / 수업하는 교수의 이름")
    private final String renterName;

    public ScheduleResponse(YearSchedule schedule) {
        if (schedule.getIsHoliday()) {
            type = ScheduleType.HOLIDAY;
            day = schedule.getDate().getDayOfWeek().getValue();
            date = schedule.getDate();
            color = schedule.getColor();
            content = schedule.getDescription();
            id = schedule.getId();
            startTime = endTime = null;
            renterName = null;
            isRenter = false;
            return;
        }
        type = ScheduleType.SPECIAL_LECTURE;
        day = schedule.getDate().getDayOfWeek().getValue();
        date = schedule.getDate();
        startTime = schedule.getStartAt();
        endTime = schedule.getEndAt();
        color = schedule.getColor();
        content = schedule.getDescription();
        id = schedule.getId();
        isRenter = false;
        renterName = null;
    }

    public ScheduleResponse(SemesterSchedule schedule, LocalDate date) {
        type = ScheduleType.LECTURE;
        day = schedule.getDay();
        this.date = date;
        startTime = schedule.getStartAt();
        endTime = schedule.getEndAt();
        color = schedule.getColor();
        content = schedule.getName();
        id = schedule.getId();
        isRenter = false;
        renterName = schedule.getProfessor().getName();
    }

    public ScheduleResponse(Classroom classroom, User renter) {
        type = ScheduleType.RENTAL;
        day = LocalDate.now().getDayOfWeek().getValue();
        date = LocalDate.now();
        startTime = classroom.getStartRentTime();
        endTime = classroom.getEndRentTime();
        color = null;
        content = classroom.getStatus().name();
        id = classroom.getId();
        isRenter = renter.getId().equals(classroom.getRenter().getId());
        renterName = renter.getName();
    }
}
