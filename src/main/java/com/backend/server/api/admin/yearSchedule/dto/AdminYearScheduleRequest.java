package com.backend.server.api.admin.yearSchedule.dto;

import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AdminYearScheduleRequest {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    @JsonProperty("isHoliday")
    private Boolean isHoliday;

    @Positive
    @Schema(example = "1")
    private Long classroomId;

    @Length(max = 20)
    private String description;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @Schema(implementation = String.class, example = "13:00")
    private LocalTime startAt;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @Schema(implementation = String.class, example = "14:00")
    private LocalTime endAt;

    public YearSchedule toEntity() {
        if (isHoliday) {
            return YearSchedule.builder()
                    .date(date)
                    .isHoliday(isHoliday)
                    .description(description)
                    .build();
        }
        return YearSchedule.builder()
                .date(date)
                .isHoliday(isHoliday)
                .classroom(Classroom.builder().id(classroomId).build())
                .description(description)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}