package com.backend.server.api.user.classroom.dto;

import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ClassroomResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String attachment;
    @Schema(example = "AVAILABLE, IN_USE, BROKEN, RENTAL_PENDING 중 하나")
    private final Status status;

    public ClassroomResponse(Classroom classroom) {
        this.id = classroom.getId();
        this.name = classroom.getName();
        this.description = classroom.getLocation();
        this.attachment = classroom.getAttachment();
        this.status = classroom.getStatus();
    }
}
