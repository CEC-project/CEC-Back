package com.backend.server.config;

import static com.backend.server.fixture.UserFixture.관리자1;

import com.backend.server.model.repository.user.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class UserSaveListener implements TestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        ApplicationContext ctx = testContext.getApplicationContext();

        UserRepository userRepository = ctx.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);

        userRepository.save(관리자1.엔티티_생성(passwordEncoder, null));
    }
}
