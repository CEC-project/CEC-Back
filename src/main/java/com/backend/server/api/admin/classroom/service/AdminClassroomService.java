package com.backend.server.api.admin.classroom.service;

import com.backend.server.api.admin.classroom.dto.AdminClassroomDetailRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.ClassroomSpecification;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminClassroomResponse> searchClassrooms(AdminClassroomSearchRequest request) {
        Specification<Classroom> spec = ClassroomSpecification.searchAndFilter(request);
        return classroomRepository.findAll(spec, request.toSort())
                .stream()
                .map((e) -> new AdminClassroomResponse(e, e.getManager()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createClassroom(AdminClassroomRequest request) {
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 관리자 ID입니다."));

        LocalTime start = request.parseStartTime();
        LocalTime end = request.parseEndTime();
        if (end.isBefore(start)) {
            throw new RuntimeException("운영 종료 시간이 시작 시간보다 빠를 수 없습니다.");
        }

        Classroom classroom = request.toEntity(manager);
        classroomRepository.save(classroom);
        return classroom.getId();
    }

    @Transactional
    public Long updateClassroom(Long id, AdminClassroomRequest request) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 강의실 ID입니다."));
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 관리자 ID입니다."));

        LocalTime start = request.parseStartTime();
        LocalTime end = request.parseEndTime();
        if (end.isBefore(start)) {
            throw new RuntimeException("운영 종료 시간이 시작 시간보다 빠를 수 없습니다.");
        }

        classroom = classroom.toBuilder()
                .name(request.getName())
                .location(request.getDescription())
                .startTime(start)
                .endTime(end)
                .manager(manager)
                .attachment(request.getAttachment())
                .build();

        classroomRepository.save(classroom);
        return classroom.getId();
    }

    @Transactional
    public void deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 강의실 ID입니다."));
        if (classroom.getStatus() == Status.IN_USE) {
            throw new RuntimeException("대여중인 강의실은 삭제할 수 없습니다.");
        }
        classroomRepository.deleteById(id);
    }

    @Transactional
    public Long markAsBroken(Long id, AdminClassroomDetailRequest request) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 강의실 ID입니다."));
        if (classroom.getStatus() == Status.BROKEN) {
            throw new RuntimeException("이미 파손된 강의실입니다.");
        }
        classroom = classroom.toBuilder()
                .status(Status.BROKEN)
                .build();
        classroomRepository.save(classroom);
        // 수리 기록 저장 로직 생략

        return classroom.getId();
    }

    @Transactional
    public Long repairClassroom(Long id, AdminClassroomDetailRequest request) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 강의실 ID입니다."));
        if (classroom.getStatus() == Status.AVAILABLE) {
            throw new RuntimeException("이미 정상 상태인 강의실입니다.");
        }
        classroom = classroom.toBuilder()
                .status(Status.AVAILABLE)
                .build();
        classroomRepository.save(classroom);
        // 수리 기록 업데이트 로직 생략

        return classroom.getId();
    }
}