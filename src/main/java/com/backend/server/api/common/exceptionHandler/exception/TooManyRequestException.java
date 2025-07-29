package com.backend.server.api.common.exceptionHandler.exception;

import static com.backend.server.support.util.DurationUtils.durationToKorean;

import com.backend.server.support.rateLimit.LimitRequestPerTime;
import java.time.Duration;
import lombok.Getter;

@Getter
public class TooManyRequestException extends RuntimeException {

    LimitRequestPerTime limit;
    Duration retryAfter;

    public TooManyRequestException(
            LimitRequestPerTime limit, Duration retryAfter) {
        super("요청 횟수 제한을 초과했습니다.");
        this.limit = limit;
        this.retryAfter = retryAfter;
    }

    public String getMessage() {
        String msg = String.format("""
                        요청 횟수 제한을 초과했습니다.
                        %d %s 동안 %d 번만 요청 가능합니다.
                        다음 요청이 가능할 때 까지 "%s" 남았습니다.
                        """,
                limit.time(),
                limit.timeUnit().toString(),
                limit.count(),
                durationToKorean(retryAfter));
        return super.getMessage() + msg;
    }
}
