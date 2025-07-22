package com.backend.server.api.common.auth.controller;

import static com.backend.server.fixture.UserFixture.MOCK_MVC_테스트시_로그인_계정;
import static com.backend.server.util.MockMvcUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.server.api.common.auth.dto.CommonSignInRequest;
import com.backend.server.api.common.auth.dto.CommonSignInResponse;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.CommonResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.mypage.dto.MyInfoResponse;
import com.backend.server.config.MockMvcConfig;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.UserRepository;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import(MockMvcConfig.class)
@DisplayName("CommonAuthController")
class CommonAuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @Nested
    class 로그인_API_는 {

        @Test
        void 사용가능한_엑세스_토큰을_응답한다() throws Exception {
            /* 1. 로그인 */
            //given
            final User user = userRepository.save(MOCK_MVC_테스트시_로그인_계정.엔티티_생성(passwordEncoder, null));
            userRepository.flush();
            final CommonSignInRequest signInRequest = MOCK_MVC_테스트시_로그인_계정.로그인_요청_생성();

            //when
            final ResultActions resultActions = mockMvc.perform(post("/api/auth/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertToJson(signInRequest)));

            //then
            resultActions.andExpect(status().isOk());

            final Cookie refreshToken = resultActions.andReturn().getResponse().getCookie("refreshToken");
            if (refreshToken == null)
                throw new Exception("No refresh token found");
            System.out.printf("refresh token : %s\n", refreshToken.getValue());

            final String responseJson = resultActions.andReturn().getResponse().getContentAsString();
            final JavaType type = objectMapper.getTypeFactory()
                    .constructParametricType(CommonResponse.class, CommonSignInResponse.class);
            final CommonResponse<CommonSignInResponse> signInResponse = objectMapper.readValue(responseJson, type);
            final String accessToken = signInResponse.getData().getAccessToken();
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

            final String responseJson2 = resultActions2.andReturn().getResponse().getContentAsString();
            final JavaType type2 = objectMapper.getTypeFactory()
                    .constructParametricType(ApiResponse.class, MyInfoResponse.class);
            final ApiResponse<MyInfoResponse> myInfoResponse = objectMapper.readValue(responseJson2, type2);
            assertThat(myInfoResponse.getData())
                    .usingRecursiveComparison()
                    .isEqualTo(expectedMyInfoResponse)
                    .as("내 정보 조회 api 가 예상과 다른 값을 반환합니다.");
            /* 2. 발급 받은 엑세스 토큰으로 내 정보 조회하기 */
        }
    }
}