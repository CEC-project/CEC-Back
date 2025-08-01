package com.backend.server.api.admin.classroom.controller;

import static com.backend.server.fixture.ClassroomFixture.강의실1;
import static com.backend.server.fixture.ClassroomFixture.강의실2;
import static com.backend.server.fixture.UserFixture.관리자1;
import static com.backend.server.support.MockMvcUtil.convertToJson;
import static com.backend.server.support.MockMvcUtil.convertToParams;
import static com.backend.server.support.MockMvcUtil.jsonPathEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.server.api.admin.classroom.dto.AdminClassroomRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest.SearchType;
import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest.SortBy;
import com.backend.server.config.ControllerTest;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.user.UserRepository;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ControllerTest
@DisplayName("AdminClassroomController")
public class AdminClassroomControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ClassroomRepository classroomRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Nested
    class 강의실_목록_조회_API는 {
        @Test
        public void 강의실명으로_검색이_가능하다() throws Exception {
            //given
            Classroom classroom = classroomRepository.save(강의실1.엔티티_생성(null));

            AdminClassroomSearchRequest request = new AdminClassroomSearchRequest();
            request.setSearchKeyword(강의실1.getName());
            request.setSearchType(SearchType.NAME);

            //when
            ResultActions result = mockMvc.perform(get("/api/admin/classroom")
                    .params(convertToParams(request)));

            //then
            AdminClassroomResponse classroomResponse = new AdminClassroomResponse(classroom, null);

            result.andExpect(status().isOk())
                    .andExpect(jsonPathEquals("$.data", List.of(classroomResponse)));
        }

        @Test
        public void ID순_정렬이_가능하다() throws Exception {
            //given
            Classroom classroom2 = classroomRepository.save(강의실2.엔티티_생성(null));
            Classroom classroom1 = classroomRepository.save(강의실1.엔티티_생성(null));

            AdminClassroomSearchRequest request = new AdminClassroomSearchRequest();
            request.setSortBy(SortBy.ID);

            //when
            ResultActions result = mockMvc.perform(get("/api/admin/classroom")
                    .params(convertToParams(request)));

            //then
            AdminClassroomResponse classroomResponse2 = new AdminClassroomResponse(classroom2, null);
            AdminClassroomResponse classroomResponse1 = new AdminClassroomResponse(classroom1, null);

            result.andExpect(status().isOk())
                    .andExpect(jsonPathEquals("$.data", List.of(classroomResponse2, classroomResponse1)));
        }
    }

    @Test
    public void 강의실_등록_API_테스트() throws Exception {
        //given
        Long managerId = userRepository.save(관리자1.엔티티_생성(passwordEncoder, null)).getId();
        AdminClassroomRequest request1 = 강의실1.등록_요청_생성(managerId);

        //when
        ResultActions result1 = mockMvc.perform(post("/api/admin/classroom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(request1)));

        //then
        result1.andExpect(status().isOk());

        String responseJson = result1.andReturn().getResponse().getContentAsString();
        Long classroomId = JsonPath.parse(responseJson).read("$.data", Long.class);

        Classroom classroom1 = classroomRepository.findById(classroomId).orElseThrow(() ->
            new AssertionError("Classroom not found"));

        강의실1.엔티티와_비교(classroom1);
    }
}
