package com.backend.server.api.user.classroom.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.classroom.dto.ClassroomActionRequest;
import com.backend.server.api.user.classroom.dto.ClassroomResponse;
import com.backend.server.api.user.classroom.dto.ScheduleResponse;
import com.backend.server.api.user.classroom.dto.ScheduleResponse.ScheduleType;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.SemesterRepository;
import com.backend.server.model.repository.classroom.SemesterScheduleRepository;
import com.backend.server.model.repository.classroom.YearScheduleRepository;
import com.backend.server.util.CompareUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final YearScheduleRepository yearScheduleRepository;
    private final SemesterScheduleRepository semesterScheduleRepository;
    private final SemesterRepository semesterRepository;

    @Transactional
    public void handleUserAction(LoginUser loginUser, ClassroomActionRequest request) {
        // 1) 사용자 로드
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2) Action → handler 맵핑
        BiConsumer<User, ClassroomActionRequest> handler = switch (request.getAction()) {
            case RENT_REQUEST   -> this::handleRentRequest;
            case RENT_CANCEL    -> this::handleRentCancel;
        };

        // 3) 단일 요청이므로 바로 호출
        handler.accept(user, request);
    }

    // --- 핸들러 메서드들 ---
    private void handleRentRequest(User user, ClassroomActionRequest request) {
        Classroom classroom = classroomRepository.findWithLockById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 강의실 id 입니다."));

        if (classroom.getStatus() != Status.AVAILABLE) {
            throw new IllegalArgumentException("현재 대여 불가한 강의실 입니다.");
        }

        LocalDate now   = LocalDate.now();
        int day         = now.getDayOfWeek().getValue();
        LocalTime start = request.getStartTime();
        LocalTime end   = request.getEndTime();

        // (1) 연간 스케줄 체크
        List<YearSchedule> yearSchedules = yearScheduleRepository.findByDate(now);
        if (yearSchedules.stream().anyMatch(YearSchedule::getIsHoliday)) {
            throw new IllegalArgumentException("휴일에는 강의실을 대여 할 수 없습니다.");
        }
        if (yearSchedules.stream().anyMatch(ys ->
                CompareUtils.hasIntersectionInclusive(ys.getStartAt(), ys.getEndAt(), start, end))) {
            throw new IllegalArgumentException("특강과 강의실 대여 시간이 겹칩니다.");
        }

        // (2) 수업 스케줄 체크
        List<Semester> semesters = semesterRepository.findSemesterContainingDate(now);
        boolean clash = semesterScheduleRepository
                .findByClassroomAndSemesterIn(classroom, semesters).stream()
                .filter(ss -> ss.getDay() == day)
                .anyMatch(ss -> CompareUtils.hasIntersectionExclusive(
                        ss.getStartAt(), ss.getEndAt(), start, end));
        if (clash) {
            throw new IllegalArgumentException("수업과 강의실 대여 시간이 겹칩니다.");
        }

        classroom.makeRentalPending(start, end, user);
        classroomRepository.save(classroom);
    }

    private void handleRentCancel(User user, ClassroomActionRequest request) {
        Classroom classroom = classroomRepository.findWithLockAndRenterById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 대여 id 입니다."));

        if (!classroom.getRenter().getId().equals(user.getId())) {
            throw new IllegalArgumentException("대여한 사용자만 취소할 수 있습니다.");
        }
        if (classroom.getStatus() != Status.RENTAL_PENDING) {
            throw new IllegalArgumentException("대여 신청 상태만 취소할 수 있습니다.");
        }

        classroom.makeAvailable();
        classroomRepository.save(classroom);
    }

    @Transactional(readOnly = true)
    public List<ClassroomResponse> getAllClassrooms() {
        return classroomRepository.findAll()
                .stream()
                .map(ClassroomResponse::new)
                .sorted(Comparator.comparing(ClassroomResponse::getId))
                .toList();
    }

    /**@param date 조회할 주차의 아무 날짜*/
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedules(LocalDate date, Long classroomId) {
        LocalDate monday = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate friday = date.plusDays(5 - date.getDayOfWeek().getValue());

        Classroom classroom = classroomRepository.findWithRenterById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 강의실 id 입니다."));

        // 조회할 주차의 연간 일정 조회
        List<YearSchedule> yearSchedules = yearScheduleRepository.findByDateBetweenAndIsHolidayTrue(monday, friday);
        yearSchedules.addAll(yearScheduleRepository.findByDateBetweenAndClassroom(monday, friday, classroom));
        List<ScheduleResponse> result = yearSchedules.stream()
                .map(ScheduleResponse::new)
                .collect(Collectors.toList());

        // 조회할 주차의 수업 시간표 조회
        List<Semester> semesters = semesterRepository.findSemesterContainingAnyDate(monday, friday);
        List<SemesterSchedule> semesterSchedules = semesterScheduleRepository
                .findByClassroomAndSemesterIn(classroom, semesters)
                .stream()
                .filter((ss) -> !monday.plusDays(ss.getDay() - 1).isAfter(ss.getSemester().getEndDate()))
                .filter((ss) -> !monday.plusDays(ss.getDay() - 1).isBefore(ss.getSemester().getStartDate()))
                .toList();
        result.addAll(semesterSchedules
                .stream()
                .map((ss) -> new ScheduleResponse(ss, monday.plusDays(ss.getDay() - 1)))
                .toList());

        // 오늘 대여된 여부 조회
        if (classroom.getRenter() != null)
            result.add(new ScheduleResponse(classroom, classroom.getRenter()));

        // 이번주의 휴일인 요일 목록 구하기
        List<Integer> holidayList = yearSchedules.stream()
                .filter(YearSchedule::getIsHoliday)
                .map((ys) -> ys.getDate().getDayOfWeek().getValue())
                .toList();

        result = result.stream()
                // 결과에서 휴일과 겹치는 일정은 제외
                .filter((sr) -> !(sr.getType() != ScheduleType.HOLIDAY && holidayList.contains(sr.getDay())))
                // 요일순, 시작 시간순으로 정렬
                .sorted((a, b) -> {
                    if (a.getDay().compareTo(b.getDay()) != 0) return a.getDay() - b.getDay();
                    return a.getStartTime().compareTo(b.getStartTime());
                })
                .toList();


        return result;
    }
}
