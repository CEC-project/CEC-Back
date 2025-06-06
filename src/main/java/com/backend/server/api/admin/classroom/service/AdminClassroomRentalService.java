package com.backend.server.api.admin.classroom.service;

import com.backend.server.api.admin.classroom.dto.AdminClassroomRentalStatusRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomDetailResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.BrokenRepairHistoryRepository;
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
    private final BrokenRepairHistoryRepository brokenRepairHistoryRepository;

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
    public List<Long> changeStatus(AdminClassroomRentalStatusRequest request) {
        BiFunction<Long, String, Long> operator = switch (request.getStatus()) {
            case ACCEPT -> (l, s) -> rentalAccept(l);
            case RETURN -> (l, s) -> rentalReturn(l);
            case CANCEL -> this::rentalCancel;
            case BROKEN -> this::rentalBroken;
            case REJECT -> this::rentalReject;
        };

        for (Long classroomId : request.getIds())
            operator.apply(classroomId, request.getDetail());
        return request.getIds();
    }

    @Transactional
    public Long rentalAccept(Long classroomId) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));
        String classroomName = classroom.getName();
        Long renterId = classroom.getRenter().getId();

        // 상태 확인
        if (classroom.getStatus() != Status.RENTAL_PENDING)
            throw new IllegalStateException("대여 요청 상태인 강의실만 승인할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom.makeInUse();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroomId, classroomName, renterId, "대여 승인", "");

        return classroomId;
    }

    @Transactional
    public Long rentalReject(Long classroomId, String detail) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));
        String classroomName = classroom.getName();
        Long renterId = classroom.getRenter().getId();

        // 상태 확인
        if (classroom.getStatus() != Status.RENTAL_PENDING)
            throw new IllegalStateException("대여 요청 상태인 강의실만 반려할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom.makeAvailable();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroomId, classroomName, renterId, "대여 반려", "\n반려 사유 : " + detail);

        return classroomId;
    }

    @Transactional
    public Long rentalReturn(Long classroomId) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));
        String classroomName = classroom.getName();
        Long renterId = classroom.getRenter().getId();

        // 상태 확인
        if (classroom.getStatus() != Status.IN_USE)
            throw new IllegalStateException("대여된 강의실만 반납할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom.makeAvailable();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroomId, classroomName, renterId, "반납 승인", "");

        return classroomId;
    }

    //강의실 파손
    @Transactional
    public Long rentalBroken(Long classroomId, String detail) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));
        String classroomName = classroom.getName();
        User renter = classroom.getRenter();
        Long renterId = renter.getId();

        // 상태 확인
        if (classroom.getStatus() != Status.IN_USE)
            throw new IllegalStateException("대여된 강의실만 반납시 파손처리 할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom.makeBroken();
        classroomRepository.save(classroom);

        BrokenRepairHistory history = BrokenRepairHistory.markAsBrokenWhenClassroomReturn(classroom, renter, detail);
        brokenRepairHistoryRepository.save(history);

        // 알림 전송
        notificationProcess(classroomId, classroomName, renterId, "반납시 파손처리", "\n파손 내용 : " + detail);

        return classroomId;
    }

    @Transactional
    public Long rentalCancel(Long classroomId, String detail) {
        // 강의실 조회
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 id가 유효하지 않습니다."));
        String classroomName = classroom.getName();
        Long renterId = classroom.getRenter().getId();

        // 상태 확인
        if (classroom.getStatus() != Status.IN_USE)
            throw new IllegalStateException("대여된 강의실만 대여 취소 할 수 있습니다. ID: " + classroomId);

        // 상태 변경
        classroom.makeAvailable();
        classroomRepository.save(classroom);

        // 알림 전송
        notificationProcess(classroomId, classroomName, renterId, "대여 취소", "\n대여 취소 사유 : " + detail);

        return classroomId;
    }

    public void notificationProcess(Long resourceId, String resourceName, Long renterId, String content, String extra) {
        // 대여자에게 알림 전송
        CommonNotificationDto notification = CommonNotificationDto.builder()
                .category("강의실 %s".formatted(content))
                .title("강의실 %s되었습니다.".formatted(content))
                .message("요청하신 강의실 [%s] 가 %s되었습니다.%s"
                        .formatted(resourceName, content, extra))
                .link("/classroom/" + resourceId)
                .build();
        notificationService.createNotification(notification, renterId);
    }
}
