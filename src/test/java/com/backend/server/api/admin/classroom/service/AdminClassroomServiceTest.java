package com.backend.server.api.admin.classroom.service;

import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.config.AbstractPostgresConfigure;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminClassroomServiceTest extends AbstractPostgresConfigure {

    @Autowired
    private AdminClassroomService adminClassroomService;

    @Test
    void searchClassrooms_전체_강의실_조회() {
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
