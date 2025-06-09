package com.backend.server.model.repository;

import com.backend.server.api.admin.professor.dto.AdminProfessorResponse;
import com.backend.server.model.entity.Professor;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query("SELECT new com.backend.server.api.admin.professor.dto.AdminProfessorResponse" +
            "(p.id, p.name, p.description, COUNT(u), p.createdAt) " +
            "FROM Professor p LEFT JOIN p.students u " +
            "GROUP BY p.id")
    List<AdminProfessorResponse> getProfessorList();

    Optional<Professor> findByName(String name);
}