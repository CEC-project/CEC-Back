package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.category.AdminCommonCategoryRequest;
import com.backend.server.api.admin.dto.category.AdminCommonCategoryResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.repository.ProfessorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminProfessorService {
    private final ProfessorRepository professorRepository;
    public List<AdminCommonCategoryResponse> getProfessorList() {
        return professorRepository.getProfessorList();
    }

    public Long createProfessor(AdminCommonCategoryRequest request) {
        Professor professor = Professor.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return professorRepository.save(professor).getId();
    }

    @Transactional
    public Long updateProfessor(Long id, AdminCommonCategoryRequest request) {
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
