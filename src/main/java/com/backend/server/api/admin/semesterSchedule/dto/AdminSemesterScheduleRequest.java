package com.backend.server.api.admin.semesterSchedule.dto;

import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSemesterScheduleRequest {
    @NotNull
    private Long professorId;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer day;

    @NotEmpty
    private String name;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime endTime;

    public SemesterSchedule toEntity(Semester semester, Professor professor, Classroom classroom) {
        return SemesterSchedule.builder()
                .semester(semester)
                .professor(professor)
                .classroom(classroom)
                .name(name)
                .startAt(startTime)
                .endAt(endTime)
                .day(day)
                .build();
    }
}