package com.backend.server.config;

import static com.backend.server.fixture.UserFixture.MOCK_MVC_테스트시_로그인_계정;

import com.backend.server.model.repository.user.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * 테스트용 관리자 계정을 db에 저장하고, 테스트가 끝나면 삭제합니다.
 */
public class UserSaveListener implements TestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        ApplicationContext ctx = testContext.getApplicationContext();

        UserRepository userRepository = ctx.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);

        userRepository.save(MOCK_MVC_테스트시_로그인_계정.엔티티_생성(passwordEncoder, null));
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        ApplicationContext ctx = testContext.getApplicationContext();

        UserRepository userRepository = ctx.getBean(UserRepository.class);

        userRepository.findByStudentNumber(MOCK_MVC_테스트시_로그인_계정.getStudentNumber())
                .ifPresent(userRepository::delete);
    }
}
