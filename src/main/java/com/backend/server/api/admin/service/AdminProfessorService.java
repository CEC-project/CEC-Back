package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.professor.AdminProfessorRequest;
import com.backend.server.api.admin.dto.professor.AdminProfessorResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.repository.professor.ProfessorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminProfessorService {
    private final ProfessorRepository professorRepository;
    public List<AdminProfessorResponse> getProfessorList() {
        return professorRepository.getProfessorList();
    }

    public Long createProfessor(AdminProfessorRequest request) {
        Professor professor = professorRepository.save(request.toEntity());
        return professor.getId();
    }

    @Transactional
    public Long updateProfessor(Long id, AdminProfessorRequest request) {
        Professor professor = professorRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        professor.toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        professorRepository.save(professor);
        return professor.getId();
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }
}
