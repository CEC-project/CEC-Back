package com.backend.server.config;

import jakarta.transaction.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;

/**
 * 로그인을 자동으로 해주어, 컨트롤러 레이어의 테스트를 쉽게 할수 있게 해주는 어노테이션입니다.<br>
 * dev 서버용 인증필터를 사용해서, 요청에 JWT 토큰이 없어도 관리자 계정으로 자동으로 인증됩니다.<br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Import({MockMvcConfig.class, FakeRateLimitConfig.class, TestDatabaseConfig.class})
@TestExecutionListeners(
        listeners = {UserSaveListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface ControllerTest {

}
