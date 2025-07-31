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
 * 1. 새로운 DB를 생성하여 테스트를 격리합니다.(테스크 클래스 마다)<br>
 * 2. @Transactional 로 테스트 메소드마다 DB 변경사항을 롤백합니다.
 * @see <a href="https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/parallel-test-execution.html">
 *     Junit5 로 병렬 테스트를 할 경우 @DirtiesContext 를 사용해선 안됩니다. (공식문서)
 * </a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional
@SpringBootTest
@Import(TestDatabaseConfig.class)
public @interface DatabaseTest {

}
