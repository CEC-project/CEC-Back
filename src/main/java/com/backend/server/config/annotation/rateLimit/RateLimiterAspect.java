package com.backend.server.config.annotation.rateLimit;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.repository.rateLimit.RateLimitRepository;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final RateLimitRepository rateLimitRepository;

    @Around("@within(LimitRequestPerTime) || @annotation(LimitRequestPerTime)")
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        final LimitRequestPerTime annotation = getAnnotation(joinPoint);
        final String key = createKey(joinPoint, annotation);

        rateLimitRepository.add(key, annotation);

        Object result = joinPoint.proceed();

        if (annotation.resetOnSuccess())
            rateLimitRepository.delete(key);

        return result;
    }

    /**
     * 1. 사용자 식별자를 해싱하여 clientKey를 생성한다.<br>
     * 2. 메소드명 + 클래스명 을 해싱하여 methodKey를 생성한다.<br>
     * 3. prefixKey + clientKey + methodKey를 반환한다.
     */
    private String createKey(ProceedingJoinPoint joinPoint, LimitRequestPerTime annotation) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 1. 사용자 식별자를 해싱하여 clientKey를 생성한다.
        String identifier;
        if (annotation.identifier().isEmpty()) {
            // 식별자 기본값은 로그인한 유저의 학번
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            identifier = loginUser.getStudentNumber();
        } else {
            // 식별자를 SpEL로 지정한 경우
            final EvaluationContext context = new StandardEvaluationContext();
            final Parameter[] params = signature.getMethod().getParameters();
            for (int i = 0; i < params.length; i++)
                context.setVariable(params[i].getName(), joinPoint.getArgs()[i]);
            identifier = parser.parseExpression(annotation.identifier()).getValue(context, String.class);
        }
        if (identifier == null || identifier.isEmpty())
            throw new IllegalArgumentException("RateLimiterAspect.createKey() : 클라이언트 식별자가 존재하지 않습니다.");
        final long clientKey = identifier.hashCode();

        // 2. 메소드명 + 클래스명 을 해싱하여 methodKey를 생성한다.
        final String className = signature.getDeclaringTypeName();
        final String methodName = signature.getName();
        final long methodKey =  String.join("#", className, methodName).hashCode();

        // 3. prefixKey + clientKey + methodKey를 반환한다.
        String prefixKey = "rate_limit";
        return String.format("%s:%d:%d", prefixKey, methodKey, clientKey);
    }

    /**
     * 메소드, 클래스, 인터페이스, 부모클래스 순으로 어노테이션을 검색하여, 처음으로 검색된 어노테이션을 반환합니다.
     */
    private LimitRequestPerTime getAnnotation(ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();

        return AnnotatedElementUtils.findMergedAnnotation(method, LimitRequestPerTime.class);
    }
}
