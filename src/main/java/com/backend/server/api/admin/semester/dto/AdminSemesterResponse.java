package com.backend.server.api.admin.semester.dto;

import com.backend.server.model.entity.classroom.Semester;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class AdminSemesterResponse {

    final private Long id;
    final private Integer year;
    final private String name;
    final private LocalDate startDate;
    final private LocalDate endDate;

    public AdminSemesterResponse(Semester semester) {
        this.id = semester.getId();
        this.year = semester.getYear();
        this.name = semester.getName();
        this.startDate = semester.getStartDate();
        this.endDate = semester.getEndDate();
    }
}