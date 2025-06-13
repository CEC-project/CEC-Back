package com.backend.server.api.admin.semesterSchedule.service;

import com.backend.server.api.admin.semesterSchedule.dto.AdminSemesterScheduleResponse;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.SemesterRepository;
import com.backend.server.model.repository.classroom.SemesterScheduleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class AdminSemesterScheduleServiceTest {

    @Autowired private AdminSemesterScheduleService semesterScheduleService;

    @Autowired private SemesterScheduleRepository semesterScheduleRepository;
    @Autowired private SemesterRepository semesterRepository;
    @Autowired private ClassroomRepository classroomRepository;

    @Test
    @DisplayName("수업 시간표 조회")
    void getSemesterSchedules() {
        //given
        Long semesterId = 1L;
        Long classroomId = 1L;
        Semester semester = semesterRepository.findById(semesterId).orElse(null);
        Classroom classroom = classroomRepository.findById(classroomId).orElse(null);

        //when
        List<AdminSemesterScheduleResponse> result =
                semesterScheduleService.getSemesterSchedules(semesterId, classroomId);

        //then

    }

    @Test
    void createSemesterSchedule() {
    }

    @Test
    void updateSemesterSchedule() {
    }

    @Test
    void deleteSemesterSchedule() {
    }
}