--[[

-- 설명
rate limit 기능을 sliding window 방식으로 구현하는 lua 언어 코드입니다.
현재 저장된 api 호출 기록을 삭제합니다.

-- 입력
key : 동일한 요청인지 판별하기 위한 식별자

-- 출력
삭제하기전 api 호출 기록의 개수를 반환합니다.

-- 내부 구현 설명
add.lua 와 동일합니다.

]]

local key = KEYS[1]
return redis.call("ZREMRANGEBYSCORE", key, 0, "inf")