package com.backend.server.api.admin.semesterSchedule.service;

import com.backend.server.api.admin.semesterSchedule.dto.AdminSemesterScheduleRequest;
import com.backend.server.api.admin.semesterSchedule.dto.AdminSemesterScheduleResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.repository.ProfessorRepository;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.SemesterRepository;
import com.backend.server.model.repository.classroom.SemesterScheduleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSemesterScheduleService {

    private final SemesterScheduleRepository semesterScheduleRepository;
    private final SemesterRepository semesterRepository;
    private final ClassroomRepository classroomRepository;
    private final ProfessorRepository professorRepository;

    @Transactional(readOnly = true)
    public List<AdminSemesterScheduleResponse> getSemesterSchedules(Long semesterId, Long classroomId) {
        Optional<Semester> optionalSemester = semesterRepository.findById(semesterId);
        if (optionalSemester.isEmpty())
            throw new IllegalArgumentException("학기 id 가 유효하지 않습니다");

        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isEmpty())
            throw new IllegalArgumentException("강의실 id 가 유효하지 않습니다");

        return semesterScheduleRepository
                .getSemesterScheduleBySemesterAndClassroom(semesterId, classroomId)
                .stream()
                .map((e) ->
                        new AdminSemesterScheduleResponse(e, e.getProfessor(), new ArrayList<>()))
                .toList();
    }

    @Transactional
    public Long createSemesterSchedule(Long semesterId, Long classroomId, AdminSemesterScheduleRequest request) {
        Optional<Semester> semester = semesterRepository.findById(semesterId);
        if (semester.isEmpty())
            throw new IllegalArgumentException("학기 id 가 유효하지 않습니다");

        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        if (classroom.isEmpty())
            throw new IllegalArgumentException("강의실 id 가 유효하지 않습니다");

        Optional<Professor> professor = professorRepository.findById(request.getProfessorId());
        if (professor.isEmpty())
            throw new IllegalArgumentException("담당교수 id 가 유효하지 않습니다");

        SemesterSchedule schedule = request.toEntity(semester.get(), professor.get(), classroom.get());
        SemesterSchedule result = semesterScheduleRepository.save(schedule);
        return result.getId();
    }

    @Transactional
    public Long updateSemesterSchedule(Long id, AdminSemesterScheduleRequest request) {
        Optional<SemesterSchedule> semesterSchedule = semesterScheduleRepository.findById(id);
        if (semesterSchedule.isEmpty())
            throw new IllegalArgumentException("수업 id 가 유효하지 않습니다");

        Optional<Professor> professor = professorRepository.findById(request.getProfessorId());
        if (professor.isEmpty())
            throw new IllegalArgumentException("담당교수 id 가 유효하지 않습니다");

        SemesterSchedule schedule = semesterSchedule.get().toBuilder()
                .professor(professor.get())
                .day(request.getDay())
                .name(request.getName())
                .startAt(request.getStartTime())
                .endAt(request.getEndTime())
                .build();
        SemesterSchedule result = semesterScheduleRepository.save(schedule);
        return result.getId();
    }

    @Transactional
    public void deleteSemesterSchedule(Long id) {
        semesterScheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("유효하지 않은 수업 시간표 id 입니다."));
        semesterScheduleRepository.deleteById(id);
    }
}