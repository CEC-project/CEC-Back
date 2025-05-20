package com.backend.server.api.admin.semester.service;

import com.backend.server.api.admin.semester.dto.AdminSemesterRequest;
import com.backend.server.api.admin.semester.dto.AdminSemesterResponse;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.repository.classroom.SemesterRepository;
import java.util.List;
import java.util.Optional;
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
        Optional<Semester> optionalSemester = semesterRepository.findById(id);
        if (optionalSemester.isEmpty())
            throw new IllegalArgumentException("학기id 가 유효하지 않습니다");

        Semester semester = optionalSemester.get().toBuilder()
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
        semesterRepository.findById(id).orElseThrow(() -> new RuntimeException("유효하지 않은 일정 ID입니다."));
        semesterRepository.deleteById(id);
    }
}