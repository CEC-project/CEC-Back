package com.backend.server.api.admin.semesterSchedule.dto;

import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.api.admin.professor.dto.AdminProfessorSimpleResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "13:00", implementation = String.class)
    final private LocalTime startAt;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "13:00", implementation = String.class)
    final private LocalTime endAt;

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
        this.startAt = entity.getStartAt();
        this.endAt = entity.getEndAt();
        this.equipmentList = equipmentList
                .stream()
                .map(AdminEquipmentResponse::new)
                .collect(Collectors.toList());
    }
}