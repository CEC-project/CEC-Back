package com.backend.server.api.user.classroom.dto;

import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ClassroomResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;

    @Schema(description = "AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING 중 하나")
    private final Status status;

    @Schema(example = "09:00", implementation = String.class, description = "운영 시작 시간")
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startTime;

    @Schema(example = "18:00", implementation = String.class, description = "운영 종료 시간")
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime endTime;

    public ClassroomResponse(Classroom classroom) {
        this.id = classroom.getId();
        this.name = classroom.getName();
        this.description = classroom.getDescription();
        this.imageUrl = classroom.getImageUrl();
        this.status = classroom.getStatus();
        this.startTime = classroom.getStartTime();
        this.endTime = classroom.getEndTime();
    }
}
