package com.backend.server.config;

import jakarta.transaction.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * DB 를 사용하는 테스트는 이 어노테이션을 쓰면 됩니다.<br>
 * 1. 새로운 DB를 생성 / 삭제하여 테스트를 격리합니다.
 * 2. @Transactional 을 통해 DB 변경사항들을 롤백합니다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional
@SpringBootTest
@Import(TestDatabaseConfig.class)
public @interface DatabaseTest {

}
