package com.backend.server.api.admin.classroom.service;

import com.backend.server.api.admin.classroom.dto.AdminClassroomRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomStatusRequest;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.RentalHistory.RentalHistoryStatus;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.ClassroomSpecification;
import com.backend.server.model.repository.history.BrokenRepairHistoryRepository;
import com.backend.server.model.repository.history.RentalHistoryRepository;
import com.backend.server.model.repository.user.UserRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
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
    private final BrokenRepairHistoryRepository brokenRepairHistoryRepository;
    private final RentalHistoryRepository rentalHistoryRepository;

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
                .attachment(request.getImageUrl())
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
        classroom.softDelete();
    }

    @Transactional
    public List<Long> changeStatus(AdminClassroomStatusRequest request, LoginUser loginUser) {
        BiFunction<Long, String, Long> operator = switch (request.getStatus()) {
            case BROKEN -> (id, detail) -> markAsBroken(id, detail, loginUser);
            case REPAIR -> (id, detail) -> repairClassroom(id, detail, loginUser);
        };

        for (Long classroomId : request.getIds()) {
            operator.apply(classroomId, request.getDetail());
        }

        return request.getIds();
    }

    @Transactional
    public Long markAsBroken(Long id, String detail, LoginUser loginUser) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 강의실 ID입니다."));

        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(()->new IllegalArgumentException("관리자를 찾을 수 없습니다"));

        if (classroom.getStatus() == Status.BROKEN)
            throw new IllegalArgumentException("이미 파손된 강의실입니다.");

        classroom.makeBroken();
        classroomRepository.save(classroom);

        BrokenRepairHistory brokenRepairHistory =
                BrokenRepairHistory.markAsBrokenClassroomByAdmin(classroom, user, detail);
        brokenRepairHistoryRepository.save(brokenRepairHistory);
        return classroom.getId();
    }

    @Transactional
    public Long repairClassroom(Long id, String detail, LoginUser loginUser) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 강의실 ID입니다."));

        if (classroom.getStatus() != Status.BROKEN)
            throw new RuntimeException("파손된 강의실만 수리 가능.");

        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Optional<BrokenRepairHistory> brokenRef =
                brokenRepairHistoryRepository.findTopByClassroomAndHistoryTypeOrderByCreatedAtDesc(
                        classroom, BrokenRepairHistory.HistoryType.BROKEN
                );

        classroom.makeAvailable();
        classroomRepository.save(classroom);

        BrokenRepairHistory repairHistory =
                BrokenRepairHistory.markAsRepairClassroom(classroom, user, detail, brokenRef.orElse(null));
        brokenRepairHistoryRepository.save(repairHistory);

        rentalHistoryRepository
                .findFirstByClassroomAndRenterOrderByCreatedAtDesc(classroom, classroom.getRenter())
                .filter(rentalHistory -> rentalHistory.getStatus().equals(RentalHistoryStatus.BROKEN))
                .ifPresent(rentalHistory -> {
                    rentalHistory.makeRepair(repairHistory);
                    rentalHistoryRepository.save(rentalHistory);
                });

        return classroom.getId();
    }
}