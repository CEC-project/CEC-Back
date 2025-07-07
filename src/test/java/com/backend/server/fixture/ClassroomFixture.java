package com.backend.server.fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.backend.server.api.admin.classroom.dto.AdminClassroomRequest;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum DefaultTime {
    START(LocalTime.of(9, 0)),
    END(LocalTime.of(18, 0));
    private final LocalTime value;
}

@AllArgsConstructor
@Getter
public enum ClassroomFixture {
    강의실1(
            "강의실1",
            "설명1",
            LocalTime.of(8, 0),
            LocalTime.of(20, 0),
            null,
            1L,
            Status.AVAILABLE),
    강의실2(
            "강의실2",
            "설명2",
            LocalTime.of(6, 0),
            LocalTime.of(18, 0),
            null,
            1L,
            Status.AVAILABLE);

    private final String name;
    private final String description;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String imageUrl;
    private final Long managerId;
    private final Status status;

    public Classroom 엔티티_생성(User manager) {
        return Classroom.builder()
                .name(name)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .imageUrl(imageUrl)
                .manager(manager)
                .status(status)
                .build();
    }

    public AdminClassroomRequest 등록_요청_생성() {
        return AdminClassroomRequest.builder()
                .name(name)
                .description(description)
                .startTime(startTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                .endTime(endTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                .imageUrl(imageUrl)
                .managerId(managerId)
                .build();
    }

    public void 엔티티와_비교(Classroom classroom) {
        assertEquals(name, classroom.getName());
        assertEquals(description, classroom.getDescription());
        assertEquals(startTime, classroom.getStartTime());
        assertEquals(endTime, classroom.getEndTime());
        assertEquals(imageUrl, classroom.getImageUrl());
        assertEquals(status, classroom.getStatus());
    }
}
