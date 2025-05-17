package com.backend.server.api.admin.yearSchedule.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminYearScheduleSearchRequest {

    @Positive
    @NotNull
    Integer year;

    @Positive
    @NotNull
    Integer month;
}
