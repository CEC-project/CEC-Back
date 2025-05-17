package com.backend.server.api.admin.yearSchedule.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.backend.server.model.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AdminYearScheduleRequest {

    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private String date;

    @NotNull
    @JsonProperty("isHoliday")
    private Boolean isHoliday;

    @Positive
    private Long classroomId;

    @Length(max = 20)
    private String description;

    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm 이어야 합니다.")
    private String startAt;

    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "시간 형식은 HH:mm 이어야 합니다.")
    private String endAt;

    public YearSchedule toEntity() {
        if (isHoliday) {
            return YearSchedule.builder()
                    .date(getLocalDate())
                    .isHoliday(isHoliday)
                    .description(description)
                    .build();
        }
        return YearSchedule.builder()
                .date(LocalDate.parse(date))
                .isHoliday(isHoliday)
                .classroom(Classroom.builder().id(classroomId).build())
                .description(description)
                .startAt(getStartTime())
                .endAt(getEndTime())
                .build();
    }

    @Schema(hidden = true)
    @JsonIgnore
    private LocalTime startTime, endTime;

    @Schema(hidden = true)
    @JsonIgnore
    private LocalDate localDate;

    public LocalTime getStartTime() {
        if (startTime != null)
            return startTime;
        return startTime = LocalTime.parse(startAt);
    }

    public LocalTime getEndTime() {
        if (endTime != null)
            return endTime;
        return endTime = LocalTime.parse(endAt);
    }

    public LocalDate getLocalDate() {
        if (localDate != null)
            return localDate;
        return localDate = LocalDate.parse(date);
    }
}