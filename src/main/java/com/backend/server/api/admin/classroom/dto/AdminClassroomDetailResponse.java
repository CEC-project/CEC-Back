package com.backend.server.api.admin.classroom.dto;

import com.backend.server.api.admin.user.dto.AdminUserResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class AdminClassroomDetailResponse {

    private final Long id;
    private final String name;
    private final String description;

    private final LocalTime startTime;
    private final LocalTime endTime;

    private final String managerName;
    private final String status;
    private final String attachment;

    private final LocalDateTime requestedTime;
    private final AdminUserResponse renter;

    public AdminClassroomDetailResponse(Classroom classroom, User manager, User renter, Professor professor) {
        this.id = classroom.getId();
        this.name = classroom.getName();
        this.description = classroom.getLocation();
        this.startTime = classroom.getStartTime();
        this.endTime = classroom.getEndTime();
        this.managerName = manager.getName(); // User의 name 필드 기준
        this.status = classroom.getStatus().name();
        this.attachment = classroom.getAttachment();
        this.requestedTime = classroom.getRequestedTime();
        this.renter = new AdminUserResponse(renter, professor);
    }
}