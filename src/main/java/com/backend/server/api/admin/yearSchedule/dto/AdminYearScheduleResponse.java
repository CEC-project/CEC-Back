package com.backend.server.api.admin.yearSchedule.dto;

import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class AdminYearScheduleResponse {

    private final LocalDate date;

    @JsonProperty("isHoliday")
    private final boolean isHoliday;

    private final AdminClassroomResponse classroom;
    private final String description;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime endTime;

    public AdminYearScheduleResponse(YearSchedule yearSchedule, AdminClassroomResponse classroom) {
        date = yearSchedule.getDate();
        isHoliday = yearSchedule.getIsHoliday();
        this.classroom = classroom;
        description = yearSchedule.getDescription();
        startTime = yearSchedule.getStartAt();
        endTime = yearSchedule.getEndAt();
    }
}