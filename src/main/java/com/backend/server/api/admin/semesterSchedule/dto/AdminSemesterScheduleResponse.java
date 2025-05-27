package com.backend.server.api.admin.semesterSchedule.dto;

import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.api.admin.professor.dto.AdminProfessorSimpleResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class AdminSemesterScheduleResponse {

    final private Long id;
    final private AdminProfessorSimpleResponse professor;
    final private Integer day;
    final private String name;
    final private String color;
    final private LocalTime startTime;
    final private LocalTime endTime;
    final private List<AdminEquipmentResponse> equipmentList;

    public AdminSemesterScheduleResponse(
            SemesterSchedule entity,
            Professor professor,
            List<Equipment> equipmentList) {
        this.id = entity.getId();
        this.professor = new AdminProfessorSimpleResponse(professor);
        this.day = entity.getDay();
        this.name = entity.getName();
        this.color = entity.getColor();
        this.startTime = entity.getStartAt();
        this.endTime = entity.getEndAt();
        this.equipmentList = equipmentList
                .stream()
                .map(AdminEquipmentResponse::new)
                .collect(Collectors.toList());
    }
}