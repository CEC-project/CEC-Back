package com.backend.server.api.admin.classroom.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AdminClassroomResponse {

    private final Long id;
    private final String name;
    private final String description;
    @Schema(description = "운영 시작 시각")
    private final String startTime;
    @Schema(description = "운영 종료 시각")
    private final String endTime;
    private final String managerName;
    private final String status;
    private final String imageUrl;

    public AdminClassroomResponse(Classroom classroom, User manager) {
        this.id = classroom.getId();
        this.name = classroom.getName();
        this.description = classroom.getLocation();
        this.startTime = classroom.getStartTime().toString();
        this.endTime = classroom.getEndTime().toString();
        this.managerName = manager.getName(); // User의 name 필드 기준
        this.status = classroom.getStatus().name();
        this.imageUrl = classroom.getAttachment();
    }
}