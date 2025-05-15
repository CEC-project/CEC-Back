package com.backend.server.api.admin.classroom.dto;

import com.backend.server.model.entity.classroom.Classroom;
import lombok.Getter;

@Getter
public class AdminClassroomResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String startTime;
    private final String endTime;
    private final String managerName;
    private final String status;
    private final String attachment;

    public AdminClassroomResponse(Classroom classroom) {
        this.id = classroom.getId();
        this.name = classroom.getName();
        this.description = classroom.getLocation();
        this.startTime = classroom.getStartTime().toString();
        this.endTime = classroom.getEndTime().toString();
        this.managerName = classroom.getManager().getName(); // User의 name 필드 기준
        this.status = classroom.getStatus().name();
        this.attachment = classroom.getAttachment();
    }
}