package com.backend.server.api.admin.dto.professor;

import com.backend.server.model.entity.Professor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProfessorRequest {
    private String name;
    private String description;

    public Professor toEntity() {
        return Professor.builder()
                .description(description)
                .name(name)
                .build();
    }
}
