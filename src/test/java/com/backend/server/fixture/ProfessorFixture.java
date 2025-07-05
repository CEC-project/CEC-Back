package com.backend.server.fixture;

import com.backend.server.api.admin.professor.dto.AdminProfessorRequest;
import com.backend.server.model.entity.Professor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfessorFixture {
    교수1("교수1", "설명1");

    private final String name;
    private final String description;

    public Professor 엔티티_생성() {
        return Professor.builder()
                .name(name)
                .description(description)
                .build();
    }

    public AdminProfessorRequest 등록_요청_생성() {
        return AdminProfessorRequest.builder()
                .name(name)
                .description(description)
                .build();
    }
}
