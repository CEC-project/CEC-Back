package com.backend.server.api.admin.yearSchedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminYearScheduleSearchRequest {

    @Positive
    @NotNull
    @Schema(example = "2025")
    Integer year;

    @Positive
    @NotNull
    @Schema(example = "5")
    Integer month;
}
