package com.backend.server.api.admin.classroom.service;

import com.backend.server.api.admin.classroom.dto.AdminClassroomDetailRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomDetailResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.ClassroomSpecification;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminClassroomRentalService {

    public final ClassroomRepository classroomRepository;
    public final UserRepository userRepository;
    private final CommonNotificationService notificationService;

    @Transactional(readOnly = true)
    public List<AdminClassroomDetailResponse> getClassrooms(AdminClassroomSearchRequest request) {
        Specification<Classroom> spec = ClassroomSpecification.searchAndFilter(request);
        return classroomRepository.findAll(spec, request.toSort())
                .stream()
                .map((c) -> {
                    User renter = c.getRenter();
                    return new AdminClassroomDetailResponse(
                            c, c.getManager(), renter, renter == null ? null : renter.getProfessor());})
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Long> changeStatus(AdminClassroomDetailRequest request) {
        BiFunction<Long, String, Long> operator = switch (request.getStatus()) {
            case ACCEPT -> (l, s) -> rentalAccept(l);
            case RETURN -> (l, s) -> rentalReturn(l);
            case CANCEL -> this::rentalCancel;
            case BROKEN -> this::rentalBroken;
            case REJECT -> this::rentalReject;
        };
        for (Long classroomId : request.getClassroomIds())
            operator.apply(classroomId, request.getDetail());
        return request.getClassroomIds();
    }

    @Transactional
    public Long rentalAccept(Long classroomId) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));

        // 상태 확인
        if (classroom.getStatus() != Status.RENTAL_PENDING)
            throw new IllegalStateException("대여 요청 상태인 강의실만 승인할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom = classroom.toBuilder().status(Status.IN_USE).build();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroom, "대여 승인", "");

        return classroomId;
    }

    @Transactional
    public Long rentalReject(Long classroomId, String detail) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));

        // 상태 확인
        if (classroom.getStatus() != Status.RENTAL_PENDING)
            throw new IllegalStateException("대여 요청 상태인 강의실만 반려할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom = classroom.toBuilder()
                .status(Status.AVAILABLE)
                .renter(null)
                .startTime(null)
                .endTime(null)
                .build();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroom, "대여 반려", "\n반려 사유 : " + detail);

        return classroomId;
    }

    @Transactional
    public Long rentalReturn(Long classroomId) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));

        // 상태 확인
        if (classroom.getStatus() != Status.IN_USE)
            throw new IllegalStateException("대여된 강의실만 반납할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom = classroom.toBuilder()
                .status(Status.AVAILABLE)
                .renter(null)
                .startTime(null)
                .endTime(null)
                .build();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroom, "반납 승인", "");

        return classroomId;
    }

    @Transactional
    public Long rentalBroken(Long classroomId, String detail) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));

        // 상태 확인
        if (classroom.getStatus() != Status.IN_USE)
            throw new IllegalStateException("대여된 강의실만 반납시 파손처리 할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom = classroom.toBuilder()
                .status(Status.BROKEN)
                .renter(null)
                .startTime(null)
                .endTime(null)
                .build();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroom, "반납시 파손처리", "\n파손 내용 : " + detail);

        return classroomId;
    }

    @Transactional
    public Long rentalCancel(Long classroomId, String detail) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));

        // 상태 확인
        if (classroom.getStatus() != Status.IN_USE)
            throw new IllegalStateException("대여된 강의실만 대여 취소 할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom = classroom.toBuilder()
                .status(Status.RENTAL_PENDING)
                .build();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroom, "대여 취소", "\n대여 취소 사유 : " + detail);

        return classroomId;
    }

    public void notificationProcess(Classroom classroom, String content, String extra) {
        Long classroomId = classroom.getId();
        Long renterId = classroom.getRenter().getId();

        // 대여자에게 알림 전송
        CommonNotificationDto notification = CommonNotificationDto.builder()
                .category("강의실 %s".formatted(content))
                .title("강의실 %s되었습니다.".formatted(content))
                .message("요청하신 강의실 [%s] 가 %s되었습니다.%s"
                        .formatted(classroom.getName(), content, extra))
                .link("/classroom/" + classroomId)
                .build();
        notificationService.createNotification(notification, renterId);
    }
}
