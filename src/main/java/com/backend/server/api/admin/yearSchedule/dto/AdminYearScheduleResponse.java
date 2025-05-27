package com.backend.server.api.admin.yearSchedule.dto;

import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class AdminYearScheduleResponse {

    private final Long id;
    private final LocalDate date;

    @JsonProperty("isHoliday")
    private final boolean isHoliday;

    private final AdminClassroomResponse classroom;
    private final String description;
    private final String color;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startAt;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime endAt;

    public AdminYearScheduleResponse(YearSchedule yearSchedule, AdminClassroomResponse classroom) {
        id = yearSchedule.getId();
        date = yearSchedule.getDate();
        isHoliday = yearSchedule.getIsHoliday();
        this.classroom = classroom;
        description = yearSchedule.getDescription();
        color = yearSchedule.getColor();
        startAt = yearSchedule.getStartAt();
        endAt = yearSchedule.getEndAt();
    }
}