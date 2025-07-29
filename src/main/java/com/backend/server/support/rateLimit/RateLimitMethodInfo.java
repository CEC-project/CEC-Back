package com.backend.server.support.rateLimit;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Rate Limit 기능에서 메소드에 관한 정보를 캐싱하는 클래스입니다.<br>
 * 1. 어노테이션이나 메소드 정보를 리플렉션으로 가져오는 부하를 줄일 수 있습니다.<br>
 * 2. SpEL parser 객체를 생성하는 부하를 줄일수 있습니다.
 */
public class RateLimitMethodInfo {

    @Getter private LimitRequestPerTime annotation;
    @Getter private long methodKey;
    @Getter private Parameter[] parameters;
    @Getter private Expression parser;
    @Getter private boolean isUsingSpEL;

    private static final ExpressionParser expressionParser = new SpelExpressionParser();
    private static final ConcurrentMap<Method, RateLimitMethodInfo> cache = new ConcurrentHashMap<>();

    private void setAnnotation(Method method) {
        // 메소드, 클래스, 인터페이스, 부모클래스 순으로 어노테이션을 검색하여, 처음으로 검색된 어노테이션을 가져옵니다.
        annotation = AnnotatedElementUtils.findMergedAnnotation(method, LimitRequestPerTime.class);
        if (annotation == null)
            throw new IllegalArgumentException("RateLimitInfo::new : 어노테이션을 찾을수 없습니다.");

        // 어노테이션이 SpEL을 사용할때만, SpEL 에 필요한 객체를 생성합니다.
        if (annotation.identifier() == null || annotation.identifier().isEmpty()) {
            isUsingSpEL = false;
            parameters = null;
            parser = null;
        } else {
            isUsingSpEL = true;
            parameters = method.getParameters();
            parser = expressionParser.parseExpression(annotation.identifier());
        }
    }

    private void setMethodKey(MethodSignature signature) {
        // 클래스의 패키지 경로 + 클래스 명 + 메소드 명 을 해싱하여, 해당 메소드만의 키를 생성합니다.
        final String className = signature.getDeclaringTypeName();
        final String methodName = signature.getName();
        methodKey = String.join("#", className, methodName).hashCode();
    }

    private RateLimitMethodInfo(MethodSignature signature, Method method) {
        setAnnotation(method);
        setMethodKey(signature);
    }

    public static RateLimitMethodInfo of(ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();

        if (!cache.containsKey(method))
            return cache.computeIfAbsent(method, (m) -> new RateLimitMethodInfo(signature, m));
        return cache.get(method);
    }
}
