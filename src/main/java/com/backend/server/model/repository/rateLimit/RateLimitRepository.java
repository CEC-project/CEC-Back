package com.backend.server.model.repository.rateLimit;

import com.backend.server.api.common.exceptionHandler.exception.TooManyRequestException;
import com.backend.server.config.annotation.rateLimit.LimitRequestPerTime;

public interface RateLimitRepository {

    /**
     * 이번 요청이 rate limit 제한에 걸리는지 판단합니다.
     * @return 이번 요청까지 포함하여, 기록이 남아있는 요청의 개수를 반환합니다.<br>
     * limit.count() 보다 클수 없습니다.
     * @throws TooManyRequestException rate limit 제한에 걸린 경우 예외를 던집니다.
     */
    Long add(String key, LimitRequestPerTime limit) throws TooManyRequestException;

    /**
     * 저장된 api 요청 기록을 삭제합니다.<br>
     * (ex : 로그인시 로그인 시도 횟수를 초기화하는 기능)
     * @return 삭제된 요청 기록의 개수를 반환합니다.
     */
    Long delete(String key);

}
