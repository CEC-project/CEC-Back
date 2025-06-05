## 앞으로 pr 승인은 코드리뷰를 거쳐야함
1. 목적 : 사전에 명세서가 제대로 만들어지지 않은 상황이므로, 스웨거로 명세서를 대신하기 위해 엄격한 규칙 적용필요.
2. 아래 규칙 준수 여부를 위주로 확인할 예정.

---

### 명세서

[사용자 웹 api 명세서 수정전](user-api-docs-before.yaml)

[사용자 웹 api 명세서 수정후](user-api-docs-after.yaml)

https://wepplication.github.io/tools/compareDoc/ 에서 수정전/후를 쉽게 비교할수 있습니다.

---

### 1. API 엔드포인트 규칙
1. URI 맨 마지막에 / 제거
2. URI 에 명사는 단수만 허용
3. 패스 파라미터가 하나라면 가능하면 "id" 로 통일
    - 목적 : /api/admin/classroom/{classroomId} 처럼 단어가 반복되는 상황 방지
4. 비슷한 기능의 API 는 합치기
    - 기능마다 요청 필드가 바뀌어야 한다면, 필드 설명에다 생략가능하다고 적기
    - ex) 강의실 대여 승인/반려/반납/승인취소 API 는 합치기
    - ex) 강의실 대여신청조회/대여승인조회 API 는 합치기
5. HTTP 메소드 구분기준
    - POST
        - API의 주 목적이 테이블에 새 레코드를 등록하는 것일때
        - 같은 요청을 반복하면 새 레코드가 계속 생길때
        - ex) 게시클/댓글/강의실/장비 등록
    - PUT
        - API의 주 목적이 특정 레코드 수정일때
        - 같은 요청을 반복해도 레코드가 동일할때(=멱등성이 있을때)
        - ex) 게시글/댓글/강의실/장비 수정
    - PATCH
        - API의 주 목적이 특정 레코드 수정일때
        - 같은 요청을 반복할떄 실패하거나 결과가 달라질때(=멱등성이 없을때)
        - ex) 반납/파손/신청/좋아요

### 2. API 요청/응답 규칙
1. 축약어 사용 금지
    - ex) direction 을 dir 로 쓰는 경우
2. ENUM 으로 처리될수 있는건 무조건 ENUM 으로, 대문자+언더바 통일
3. 파라미터/ENUM 이름 정할때 다른 비슷한 API 들 꼼꼼히 찾아보고 정하기
    - ex) 검색어 파라미터는 searchKeyword 로 통일
    - ex) 사용자 이름 기준으로 검색할시, 검색 유형은 searchType=NAME 로 통일
    - ex) 통합 검색은 searchType=ALL 또는 생략으로 통일

### 3. 스웨거 설명 작성 규칙
1. 요청/응답 필드별 설명
    - 조회 API 는 모든 요청 필드에 설명이 있어야함.
    - DTO 내의 필드에 대한 설명은 DTO 내에 적기
        - 목적 : 수정하거나 복붙하고 관리가 안되는 경우가 많기 떄문.
    - 요청/응답 필드에 대한 설명을 DTO에 @Schema(description="설명") 이용해서 적기
        - 이름/타입/ENUM값 만으로 의도가 충분히 전달된다면 적지 않아도 괜찮음.
    - LocalTime, LocalDate, LocalDateTime 등 시간 타입으로 응답할떄는 스웨거에서 잘 뜨는지 확인하고 조치하기
        - LocalDateTime 은 기본 형식(ISO-8601)으로 넘겨주면 프론트에서 알아서 파싱하기로 합의됨.
        - LocalTime 은 별도 설정하지 않으면 스웨거에 잘못 표시되므로 주의.
            - [AdminClassroomDetailResponse](/src/main/java/com/backend/server/api/admin/classroom/dto/AdminClassroomDetailResponse.java) 참고
    - 모든 요청/응답 필드가 스웨거에서 정상적으로 보이는지 확인해야함.
2. 컨트롤러별 설명
    - @Tag(name="`상위메뉴번호`-`하위메뉴번호`. `상위메뉴명` / `하위메뉴명`", description="`작업 완료` or `수정 필요`")
    - ex) @Tag(name="3-2. 강의실/장비 관리 / 강의실 관리", description="작업 완료")
    - ex) @Tag(name="4-2. 사용자 관리 / 제재 목록", description="수정 필요")
    - 메뉴별 이름과 번호는 프론트의 메뉴를 기준으로 함.
        - [관리자 웹](https://admin.bmvcec.store/)
        - [사용자 웹](https://bmvcec.store/)
        - [피그마](https://www.figma.com/design/akBPeol86QXSt1FbTEEc6P/%EC%9E%A5%EB%B9%84-%EB%8C%80%EC%97%AC-%EC%9B%B9-%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%ED%99%94%EB%A9%B4-%EC%84%A4%EA%B3%84) 
3. 설명에 말 줄임표 "..." 사용하지 않기.