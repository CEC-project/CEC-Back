### 사용자 웹 명세서

지금 관리자 웹에서 프론트랑 연동하는 중에, 파라미터나 응답값 통일성 문제가 있어서,
사용자 웹 쪽은 매끄럽게 작업하기 위해 사용자 웹 명세서를 다시 만들었습니다.

- [사용자 웹 api 명세서](user-api-docs-after.yaml)
- [사용자 웹 api 명세서(6월 5일 현재 상태)](user-api-docs-before.yaml)

위 파일은 1800줄 정도 되므로, 컨트롤러 단위로 짧게 분리한 명세서는 [after](./after) 와 [before](./before) 폴더에서 볼수 있습니다. 

파일 내용을 https://editor.swagger.io/ 사이트에 붙여넣거나, 인텔리제이로 보면 편하게 볼수 있습니다.
[비교 사이트](https://wepplication.github.io/tools/compareDoc/) 에서 두 파일을 비교하면 뭘 고쳐야하는지 쉽게 보입니다.

### 명세서 주요 변경사항
1. 강의실/장비/프로필 사진은 imageUrl 로 통일하고, 게시글/문의 첨부파일 응답은 attachments 로 통일했습니다.
   - 구분한 기준은 사진이냐 아니냐, 파일이 하나냐 아니냐 입니다.
   - attachments 는 배열로 응답하는 것이 맞는것 같습니다.
2. 게시글/댓글 작성자는 authorResponse dto를 반환하도록 통일했습니다.
3. 문의 상세조회 응답에서 문의 답변도 응답하게 수정했습니다.
4. community 를 전부 board 로 수정하고, url 를 수정했습니다.
5. 다른 자잘한 수정사항도 있으니, 각자 맡은 부분 명세서를 [after](./after) 와 [before](./before) 폴더에서 찾아서 [비교 사이트](https://wepplication.github.io/tools/compareDoc/)에서 비교해 주세요.

### 6월 6일 명세서 변경사항
1. 댓글 관련 필드중 type -> targetType 으로 수정하는게 좋겠습니다.
   - type 이라는 이름만 보고 무엇인지 유추하기 힘들다고 생각합니다.
   - 같은 dto 의 targetId 필드와 관련이 깊으므로, 두 필드가 공통으로 target 접두사를 쓰는게 좋겠습니다.
   - GET /api/admin/broken-repair-history api 에서도 targetType 이라는 필드가 있어서, 통일하면 좋겠습니다.
2. GET /api/board/post/{id}/recommend 를 추가했습니다.
   - 추천을 여부를 조회하는 API 가 필요해서 추가했습니다. 
3. 기존 POST /api/board/post/{id}/recommend 를 PATCH /api/board/post/{id}/recommend 로 수정했습니다.
   - 이미 추천을 했다면 에러를 던지는 것보다, 추천을 취소하는거 어떨까요?
   - 다른 의견 있다면 말씀해 주세요.
4. 위 내용들은 yaml 파일들에도 반영되었습니다.

---

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