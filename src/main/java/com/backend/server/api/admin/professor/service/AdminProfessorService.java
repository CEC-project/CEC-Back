package com.backend.server.api.admin.professor.service;

import com.backend.server.api.admin.professor.dto.AdminProfessorRequest;
import com.backend.server.api.admin.professor.dto.AdminProfessorResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.ProfessorRepository;
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
        Professor professor = Professor.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return professorRepository.save(professor).getId();
    }

    @Transactional
    public Long updateProfessor(Long id, AdminProfessorRequest request) {
        Professor professor = professorRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        professor = professor.toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        professor = professorRepository.save(professor);
        return professor.getId();
    }

    //public void deleteProfessor(Long id) {
        //professorRepository.deleteById(id);
    //}

    @Transactional
    public void deleteProfessor(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        professor.softDelete();
        professorRepository.save(professor);
    }
}
