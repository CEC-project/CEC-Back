package com.backend.server.api.common.auth.controller;

import static com.backend.server.fixture.UserFixture.MOCK_MVC_테스트시_로그인_계정;
import static com.backend.server.util.MockMvcUtil.convertToJson;
import static com.backend.server.util.MockMvcUtil.toDto;
import static com.backend.server.util.MockMvcUtil.toJsonPathDocument;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.server.api.common.auth.dto.CommonSignInRequest;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.mypage.dto.MyInfoResponse;
import com.backend.server.config.MockMvcConfig;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.rateLimit.RateLimitRepository;
import com.backend.server.model.repository.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import(MockMvcConfig.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("CommonAuthController")
class CommonAuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;

    @MockitoSpyBean private RateLimitRepository rateLimitRepository;

    final private List<String> capturedKeys = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //로그인 테스트 진행할 계정 등록
        userRepository.save(MOCK_MVC_테스트시_로그인_계정.엔티티_생성(passwordEncoder, null));

        //redis 에 추가되는 rate limit 관련 key 를 캡처.
        doAnswer(invocation -> {
            String key = (String) invocation.getArguments()[0];
            capturedKeys.add(key);
            return invocation.callRealMethod();
        }).when(rateLimitRepository).add(any(), any());
    }

    @AfterEach
    void tearDown() {
        //다음 테스트에 영향이 없도록, 캡쳐한 rate limit 관련 key 들을 redis 에서 삭제.
        capturedKeys.forEach(rateLimitRepository::delete);
    }

    @Nested
    class 로그인_API_는 {

        @Test
        void 사용가능한_엑세스_토큰을_응답한다() throws Exception {
            /* 1. 로그인 */
            //given
            final User user = userRepository
                    .findByStudentNumber(MOCK_MVC_테스트시_로그인_계정.getStudentNumber())
                    .orElseThrow();

            //when
            final ResultActions resultActions = 올바른_로그인_요청을_보낸다();

            //then
            resultActions.andExpect(status().isOk());

            final Cookie refreshToken = resultActions.andReturn().getResponse().getCookie("refreshToken");
            if (refreshToken == null)
                throw new Exception("No refresh token found");
            System.out.printf("refresh token : %s\n", refreshToken.getValue());

            final String accessToken = toJsonPathDocument(resultActions).read("$.data.accessToken");
            System.out.printf("access token : %s\n", accessToken);
            /* 1. 로그인 */

            /* 2. 발급 받은 엑세스 토큰으로 내 정보 조회하기 */
            //given
            final MyInfoResponse expectedMyInfoResponse = new MyInfoResponse(new LoginUser(user));

            //when
            final ResultActions resultActions2 = mockMvc.perform(get("/api/mypage")
                    .header("Referer", "http://localhost:3000")
                    .header("Authorization", "Bearer " + accessToken));

            //then
            resultActions2.andExpect(status().isOk());

            final ApiResponse<MyInfoResponse> myInfoResponse = toDto(resultActions2, new TypeReference<>() {});
            assertThat(myInfoResponse.getData())
                    .usingRecursiveComparison()
                    .isEqualTo(expectedMyInfoResponse)
                    .as("내 정보 조회 api 가 예상과 다른 값을 반환합니다.");
            /* 2. 발급 받은 엑세스 토큰으로 내 정보 조회하기 */
        }
        
        @Test
        void 일분간_연속_5회_실패시_Rate_Limit_에_걸린다() throws Exception {
            /* 1. 연속 5회 실패 */
            for (int i = 0; i < 5; i++)
                비밀번호가_틀린_로그인_요청을_보낸다().andExpect(status().isInternalServerError());
            
            /* 2. 6회 째에 올바르게 로그인 정보를 기입해도 429(Too many request) 에러 발생 */
            올바른_로그인_요청을_보낸다().andExpect(status().isTooManyRequests());
        }

        @Test
        void 로그인_성공시_실패_횟수가_초기화_된다() throws Exception {
            // 1. 4회 실패
            for (int i = 0; i < 4; i++)
                비밀번호가_틀린_로그인_요청을_보낸다().andExpect(status().isInternalServerError());

            // 2. 1회 성공
            올바른_로그인_요청을_보낸다().andExpect(status().isOk());

            // 3. 4회 싶패
            for (int i = 0; i < 4; i++)
                비밀번호가_틀린_로그인_요청을_보낸다().andExpect(status().isInternalServerError());

            // 4. 1회 성공
            올바른_로그인_요청을_보낸다().andExpect(status().isOk());
        }

        private ResultActions 올바른_로그인_요청을_보낸다() throws Exception {
            //given
            final CommonSignInRequest correctSignInRequest = MOCK_MVC_테스트시_로그인_계정.로그인_요청_생성();

            //when
            return mockMvc.perform(post("/api/auth/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertToJson(correctSignInRequest)));
        }

        private ResultActions 비밀번호가_틀린_로그인_요청을_보낸다() throws Exception {
            //given
            final CommonSignInRequest wrongSignInRequest = MOCK_MVC_테스트시_로그인_계정.로그인_요청_생성();
            wrongSignInRequest.setPassword("wrongPassword");

            //when
            return mockMvc.perform(post("/api/auth/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertToJson(wrongSignInRequest)));
        }
    }
}