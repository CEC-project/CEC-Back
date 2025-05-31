package com.backend.server.api.user.classroom.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ScheduleResponse {
    public enum ScheduleType {
        HOLIDAY, SPECIAL_LECTURE, LECTURE, RENTAL
    }

    @Schema(example = "HOLIDAY, SPECIAL_LECTURE, LECTURE, RENTAL 중 하나.")
    private final ScheduleType type;

    @Schema(description = "요일을 나타냄. 1, 2, 3, 4, 5 중 하나.")
    private final Integer day;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "13:00", implementation = String.class, description = "일정 시작 시간")
    private final LocalTime startAt;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "13:00", implementation = String.class, description = "일정 종료 시간")
    private final LocalTime endAt;

    private final String color;

    @Schema(description = "강의실의 경우, 이 값은 항상 대여 됨 입니다.")
    private final String content;

    @Schema(description = "대여 취소할때는 이 값을 보내면 됩니다.")
    private final Long id;

    public ScheduleResponse(YearSchedule schedule) {
        if (schedule.getIsHoliday()) {
            type = ScheduleType.HOLIDAY;
            day = schedule.getDate().getDayOfWeek().getValue();
            date = schedule.getDate();
            color = schedule.getColor();
            content = schedule.getDescription();
            id = schedule.getId();
            startAt = endAt = null;
            return;
        }
        type = ScheduleType.SPECIAL_LECTURE;
        day = schedule.getDate().getDayOfWeek().getValue();
        date = schedule.getDate();
        startAt = schedule.getStartAt();
        endAt = schedule.getEndAt();
        color = schedule.getColor();
        content = schedule.getDescription();
        id = schedule.getId();
    }

    public ScheduleResponse(SemesterSchedule schedule, LocalDate date) {
        type = ScheduleType.LECTURE;
        day = schedule.getDay();
        this.date = date;
        startAt = schedule.getStartAt();
        endAt = schedule.getEndAt();
        color = schedule.getColor();
        content = schedule.getName();
        id = schedule.getId();
    }

    public ScheduleResponse(Classroom classroom, User renter) {
        type = ScheduleType.RENTAL;
        day = LocalDate.now().getDayOfWeek().getValue();
        date = LocalDate.now();
        startAt = classroom.getStartRentTime();
        endAt = classroom.getEndRentTime();
        color = null;
        content = "대여됨";
        id = renter.getId();
    }
}
