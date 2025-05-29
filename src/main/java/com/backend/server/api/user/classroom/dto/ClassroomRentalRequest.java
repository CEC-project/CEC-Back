package com.backend.server.api.user.classroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassroomRentalRequest {
    @JsonFormat(pattern = "HH:mm")
    @NotNull
    @Schema(implementation = String.class, example = "13:00")
    private LocalTime startAt;

    @JsonFormat(pattern = "HH:mm")
    @NotNull
    @Schema(implementation = String.class, example = "14:00")
    private LocalTime endAt;
}
