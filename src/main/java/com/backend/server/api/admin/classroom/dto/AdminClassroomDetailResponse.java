package com.backend.server.api.admin.classroom.dto;

import com.backend.server.api.admin.user.dto.AdminUserResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class AdminClassroomDetailResponse {

    private final Long id;
    private final String name;
    private final String description;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "14:00", description = "대여 시작 시간", implementation = String.class)
    private final LocalTime startRentTime;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "16:00", description = "대여 종료 시간", implementation = String.class)
    private final LocalTime endRentTime;

    private final Long managerId;
    private final String managerName;
    private final String status;
    private final String attachment;

    private final LocalDateTime requestedTime;
    private final AdminUserResponse renter;

    public AdminClassroomDetailResponse(Classroom classroom, User manager, User renter, Professor professor) {
        this.id = classroom.getId();
        this.name = classroom.getName();
        this.description = classroom.getLocation();
        this.startRentTime = classroom.getStartRentTime();
        this.endRentTime = classroom.getEndRentTime();
        this.status = classroom.getStatus().name();
        this.attachment = classroom.getAttachment();
        this.requestedTime = classroom.getRequestedTime();
        this.managerName = manager != null ? manager.getName() : null;
        this.managerId = manager != null ? manager.getId() : null;
        this.renter = renter != null ? new AdminUserResponse(renter, professor) : null;
    }
}