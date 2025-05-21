package com.backend.server.api.admin.semester.service;

import com.backend.server.api.admin.semester.dto.AdminSemesterRequest;
import com.backend.server.api.admin.semester.dto.AdminSemesterResponse;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.repository.classroom.SemesterRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSemesterService {

    private final SemesterRepository semesterRepository;

    @Transactional(readOnly = true)
    public List<AdminSemesterResponse> getSemesters() {
        return semesterRepository.findAllByOrderByStartDateAsc()
                .stream()
                .map(AdminSemesterResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createSemester(AdminSemesterRequest request) {
        Semester entity = request.toEntity();
        Semester result = semesterRepository.save(entity);
        return result.getId();
    }

    @Transactional
    public Long updateSemester(Long id, AdminSemesterRequest request) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("학기 id 가 유효하지 않습니다"))
                .toBuilder()
                .year(request.getYear())
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Semester result = semesterRepository.save(semester);
        return result.getId();
    }

    @Transactional
    public void deleteSemester(Long id) {
        semesterRepository.findById(id).orElseThrow(() -> new RuntimeException("유효하지 않은 학기 id 입니다."));
        semesterRepository.deleteById(id);
    }
}