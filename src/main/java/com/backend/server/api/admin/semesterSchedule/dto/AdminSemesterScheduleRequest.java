package com.backend.server.api.admin.semesterSchedule.dto;

import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSemesterScheduleRequest {
    @NotNull
    @Positive
    @Schema(example = "1")
    private Long professorId;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer day;

    @NotEmpty
    private String name;
    private String color;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    @Schema(implementation = String.class, example = "13:00")
    private LocalTime startAt;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    @Schema(implementation = String.class, example = "14:00")
    private LocalTime endAt;

    @NotNull
    @Schema(example = "[2]")
    private List<@NotNull @Positive Long> equipments;

    public SemesterSchedule toEntity(
            Semester semester,
            Professor professor,
            Classroom classroom) {
        return SemesterSchedule.builder()
                .semester(semester)
                .professor(professor)
                .classroom(classroom)
                .name(name)
                .color(color)
                .startAt(startAt)
                .endAt(endAt)
                .day(day)
                .build();
    }
}