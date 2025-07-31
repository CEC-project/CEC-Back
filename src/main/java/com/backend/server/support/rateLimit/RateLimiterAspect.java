package com.backend.server.support.rateLimit;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.repository.rateLimit.RateLimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RateLimitRepository rateLimitRepository;

    @Around("@within(com.backend.server.support.rateLimit.LimitRequestPerTime) || @annotation(com.backend.server.support.rateLimit.LimitRequestPerTime)")
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        final RateLimitMethodInfo info = RateLimitMethodInfo.of(joinPoint);
        final String key = createKey(joinPoint, info);

        rateLimitRepository.add(key, info.getAnnotation());

        Object result = joinPoint.proceed();

        if (info.getAnnotation().resetOnSuccess())
            rateLimitRepository.delete(key);

        return result;
    }

    /**
     * 1. 사용자 식별자를 해싱하여 clientKey를 생성한다.<br>
     * 2. prefixKey + clientKey + methodKey를 반환한다.
     */
    private String createKey(ProceedingJoinPoint joinPoint, RateLimitMethodInfo info) {
        // 1. 사용자 식별자를 해싱하여 clientKey를 생성한다.
        String identifier;
        if (info.isUsingSpEL()) {
            // 식별자를 SpEL로 지정한 경우
            final EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < info.getParameters().length; i++)
                context.setVariable(info.getParameters()[i].getName(), joinPoint.getArgs()[i]);
            identifier = info.getParser().getValue(context, String.class);
        } else {
            // 식별자 기본값은 로그인한 유저의 학번
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            identifier = loginUser.getStudentNumber();
        }
        if (identifier == null || identifier.isEmpty())
            throw new IllegalArgumentException("RateLimiterAspect.createKey() : 클라이언트 식별자가 존재하지 않습니다.");
        final long clientKey = identifier.hashCode();

        // 2. prefixKey + clientKey + methodKey를 반환한다.
        String prefixKey = "rate_limit";
        return String.format("%s:%d:%d", prefixKey, info.getMethodKey(), clientKey);
    }
}
