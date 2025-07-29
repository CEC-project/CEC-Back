package com.backend.server.support.rateLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * rate limit (요청 횟수 제한)을 적용시키는 어노테이션 입니다.<br>
 * 사용자 식별자와 패키지경로+클래스명+메소드명 을 기준으로 동일한 요청인지 판단합니다.<br>
 * 어노테이션이 중복으로 적용된 경우 "메소드, 클래스, 인터페이스, 부모클래스" 중에서 가장 앞에 있는 것이 적용됩니다.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRequestPerTime {

    /**
     * SpEL 표현식으로 매개변수에서 사용자 식별자(String 타입)를 가져와 사용할수 있습니다.<br>
     * (기본값 : 로그인한 계정의 사용자 학번)<br>
     * 예를 들어, 이 필드에 "Sting.valueOf(#a.getId())" 을 대입한 경우 아래와 같은 뜻이 됩니다.<br>
     * 이 어노테이션이 붙은 메소드에 a 라는 매개변수가 있다. a.getId() 메소드를 호출한 결과를 문자열로 변환시켜서 사용자 식별자로 사용하겠다.
     */
    String identifier() default "";

    /**
     * 요청 제한 시간<br>
     * (기본값 : 1)
     */
    int time() default 1;

    /**
     * 요청 제한 시간 단위<br>
     * (기본값 : TimeUnit.MINUTES)
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 요청 제한 횟수<br>
     * (기본값 : 5)
     */
    int count() default 5;

    /**
     * 이 어노테이션이 붙은 메소드가 예외 throw 없이 정상적으로 반환되었을때, 기존 요청 기록을 삭제하는 기능<br>
     * (기본값 : false)<br>
     * 예를 들어, 로그인 성공시 로그인 실패 횟수를 초기화 시키는 것을 구현할수 있습니다.
     */
    boolean resetOnSuccess() default false;
}
