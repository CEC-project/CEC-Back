package com.backend.server.api.admin.professor.dto;

import com.backend.server.model.entity.Professor;
import lombok.Getter;

@Getter
public class AdminProfessorSimpleResponse {
    final private Long id;
    final private String name;

    public AdminProfessorSimpleResponse(Professor entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
