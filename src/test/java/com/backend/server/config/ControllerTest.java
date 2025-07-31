package com.backend.server.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;

/**
 * 실제 요청을 통한 테스트를 쉽게 작성하게 해주는 어노테이션입니다.<br>
 * 1. 새로운 DB를 생성 / 삭제하여 테스트를 격리합니다.(테스크 클래스 마다)<br>
 * 2. @Transactional 을 이용하여 테스트 메소드마다 DB 변경사항을 롤백합니다.<br>
 * 3. 요청에 JWT 토큰이 없어도 관리자 계정으로 자동으로 인증됩니다.<br>
 * 4. api rate limit 제한을 해제합니다. (분당 5회 호출 제한 등등을 해제)<br>
 * (인증을 위해 관리자 계정을 DB에 추가합니다.)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DatabaseTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Import({MockMvcConfig.class, FakeRateLimitConfig.class, TestDatabaseConfig.class})
@TestExecutionListeners(
        listeners = {UserSaveListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface ControllerTest {

}
