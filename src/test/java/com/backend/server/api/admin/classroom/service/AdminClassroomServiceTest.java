package com.backend.server.api.admin.classroom.service;

import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminClassroomServiceTest {

    @Autowired
    private AdminClassroomService adminClassroomService;

    @Test
    @DisplayName("전체 강의실 조회")
    void searchClassrooms() {
        // given
        AdminClassroomSearchRequest request = new AdminClassroomSearchRequest();

        // when
        List<AdminClassroomResponse> response = adminClassroomService.searchClassrooms(request);

        // then
        System.out.println(response.stream()
                .map((r) -> "%d : %s %s %s".formatted(r.getId(), r.getName(), r.getDescription(), r.getStatus()))
                .reduce((a, b) -> a + '\n' + b));
    }
}
