--[[

-- 설명
rate limit 기능을 sliding window 방식으로 구현하는 lua 언어 코드입니다.
api 호출이 제한 횟수을 넘는지 판단합니다.

-- 입력
1. key : 동일한 요청인지 판별하기 위한 식별자
2. windowMillis : 밀리초 단위 윈도우 크기
3. threshold : 요청 횟수 상한

-- 출력
1. 정상적인 요청인 경우 : 양수로 이번 요청을 포함한 현재까지의 요청 횟수를 반환합니다.
2. 요청 횟수 상한을 초과한 요청인 경우 : 음수로 window 내에서 가장 오래된 요청의 시간을 밀리초 단위로 반환합니다.

-- 내부 구현 설명 1
아래 코드를 보면 ZADD 나 ZRANGE 처럼 Z로 시작하는 명령어들을 사용하고 있습니다.
Z로 시작하는 redis 명령어들은 redis 의 sorted sets 데이터 타입을 사용하는 명령어 입니다.
sorted sets 은 동일한 key 에 여러 멤버를 저장할수 있고, 특정 구간의 멤버를 삭제하거나 조회하는게 가능한 데이터 타입입니다.
특정 기간동안 몇 회 동일한 요청이 있었는지 알아야 하고, 해당 기간에서 벗어난 데이터는 삭제해야 하기 때문에 sorted sets 을 사용했습니다.
(각 key 마다 red black tree 가 생성된다고 연상하면 이해가 쉬울거 같습니다.)
관련 문서 : https://redis.io/docs/latest/develop/data-types/sorted-sets/

-- 내부 구현 설명 2
아래 코드는 redis 의 eval 또는 evalsha 명령어에 인자로 넘겨져서 실행되게 됩니다.
redis 의 eval, evalsha 명령어는 원자성을 보장하고, 성능도 향상시키는 유용한 명령어입니다.
(redis cluster 같은 분산환경에서 적용하여 동시성 문제를 해결한 사례도 찾을 수 있습니다.)
spring data redis 의 RedisTemplate 과 함께 사용하면, 기본적으로 최초 1회 실행시만 스크립트 전체가 전송됩니다.
redis functions 기능을 사용하면 라이브러리 형태로 코드를 관리할 수도 있는데, 관리하는 노력이 추가로 들 것 같아서 적용하지 않았습니다.
관련 문서 : https://redis.io/docs/latest/develop/programmability/eval-intro/

]]

-- 인자를 받음
local key = KEYS[1]
local windowMillis = tonumber(ARGV[1])
local threshold = tonumber(ARGV[2])

-- 현재 시간을 밀리초 단위로 읽음
local t = redis.call('TIME')
local nowMillis = tonumber(t[1]) * 1000 + math.floor(tonumber(t[2]) / 1000)

-- window 범위를 벗어난 요청 기록들을 삭제
redis.call("ZREMRANGEBYSCORE", key, 0, nowMillis - windowMillis)

-- 요청 기록의 개수를 조회
local count = redis.call("ZCARD", key)
if count >= threshold then
    -- count 가 threshold 를 넘겼다면, 음수로 window 범위에서 가장 오래된 요청 시간을 반환
    local firstRequestTime = redis.call("ZRANGE", key, 0, 0)[1]
    return -tonumber(firstRequestTime)
end

-- 새 요청 기록 추가
redis.call("ZADD", key, nowMillis, nowMillis)

-- 메모리 낭비를 막기 위해, Key 에 만료시간 걸기
redis.call("EXPIRE", key, (windowMillis / 1000) + 1)

-- 요청 기록의 개수 반환
return count + 1