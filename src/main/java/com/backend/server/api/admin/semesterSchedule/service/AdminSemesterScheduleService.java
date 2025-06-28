package com.backend.server.api.admin.semesterSchedule.service;

import com.backend.server.api.admin.semesterSchedule.dto.AdminSemesterScheduleRequest;
import com.backend.server.api.admin.semesterSchedule.dto.AdminSemesterScheduleResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.user.ProfessorRepository;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.SemesterRepository;
import com.backend.server.model.repository.classroom.SemesterScheduleRepository;
import java.util.List;

import com.backend.server.model.repository.equipment.EquipmentRepository;
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
    private final EquipmentRepository equipmentRepository;

    @Transactional(readOnly = true)
    public List<AdminSemesterScheduleResponse> getSemesterSchedules(Long semesterId, Long classroomId) {
        semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("학기 id 가 유효하지 않습니다"));

        classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id 가 유효하지 않습니다"));

        return semesterScheduleRepository
                .getSemesterScheduleBySemesterAndClassroom(semesterId, classroomId)
                .stream()
                .map((e) -> new AdminSemesterScheduleResponse(e, e.getProfessor(), e.getEquipments()))
                .toList();
    }

    @Transactional
    public Long createSemesterSchedule(Long semesterId, Long classroomId, AdminSemesterScheduleRequest request) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("학기 id 가 유효하지 않습니다"));

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id 가 유효하지 않습니다"));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new IllegalArgumentException("담당교수 id 가 유효하지 않습니다"));

        SemesterSchedule schedule = request.toEntity(semester, professor, classroom);
        SemesterSchedule result = semesterScheduleRepository.save(schedule);

        List<Long> equipments = request.getEquipments();
        if (equipments != null && !equipments.isEmpty()) {
            if (!equipmentRepository.isAvailableAllByIdIn(equipments, equipments.size(), Status.AVAILABLE))
                throw new IllegalArgumentException("장비 id 가 유효하지 않습니다");

            equipmentRepository.rentByIds(
                    equipments,
                    semester.getStartDate().atStartOfDay(),
                    semester.getEndDate().atStartOfDay(),
                    result,
                    Status.IN_USE);
        }

        return result.getId();
    }

    @Transactional
    public Long updateSemesterSchedule(Long id, AdminSemesterScheduleRequest request) {
        SemesterSchedule semesterSchedule = semesterScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수업 id 가 유효하지 않습니다"));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new IllegalArgumentException("담당교수 id 가 유효하지 않습니다"));

        SemesterSchedule schedule = semesterSchedule.toBuilder()
                .professor(professor)
                .day(request.getDay())
                .name(request.getName())
                .color(request.getColor())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .build();
        SemesterSchedule result = semesterScheduleRepository.save(schedule);

        Semester semester = semesterRepository.findById(schedule.getSemester().getId())
                .orElseThrow(() -> new IllegalArgumentException("학기 id 가 유효하지 않습니다"));

        List<Long> equipments = request.getEquipments();
        if (equipments != null && !equipments.isEmpty()) {
            if (!equipmentRepository.isAvailableAllByIdIn(equipments, equipments.size(), schedule, Status.AVAILABLE))
                throw new IllegalArgumentException("장비 id 가 유효하지 않습니다");

            // 제외된 장비는 대여 취소하는 코드 필요

            equipmentRepository.rentByIds(
                    equipments,
                    semester.getStartDate().atStartOfDay(),
                    semester.getEndDate().atStartOfDay(),
                    result,
                    Status.IN_USE);
        }

        return result.getId();
    }

    @Transactional
    public void deleteSemesterSchedule(Long id) {
        SemesterSchedule schedule = semesterScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 수업 시간표 id 입니다."));
        equipmentRepository.cancelRent(schedule.getEquipments(), Status.AVAILABLE);
        schedule.softDelete();
    }
}