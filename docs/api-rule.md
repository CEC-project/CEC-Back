1. 앞으로 pr 승인은 코드리뷰를 거쳐야함
    1. 목적 : 사전에 명세서가 제대로 만들어지지 않았으므로, 스웨거로 명세서를 대신하기 위해 엄격한 규칙 적용필요.
    2. 아래 규칙 준수 여부를 위주로 확인할 예정.
2. API 엔드포인트 규칙
    1. URI 맨 마지막에 / 제거
    2. 단수/복수, 동사/명사 사용등은 개인 재량에 맡김
    3. POST
        - API의 주 목적이 테이블에 새 레코드를 등록하는 것일때
        - ex) 게시클/댓글/강의실/장비 등록
    4. PUT
        - API의 주 목적이 특정 레코드 수정이고, 같은 요청을 반복해도 레코드가 동일할때
        - ex) 게시글/댓글/강의실/장비 수정
    5. PATCH
        - API의 주 목적이 수정이고, 같은 요청을 반복할떄 실패하거나 결과가 달라질때
        - ex) 반납/신청/좋아요
    6. 비슷한 기능의 API 는 합치기
        - ex) 강의실 대여 승인/반려/반납/승인취소 API 는 합치기
        - ex) 강의실 대여신청조회/대여승인조회 API 는 합치기
3. API 요청/응답 규칙
    1. 축약어 사용 금지
        - ex) direction 을 dir 로 쓰는 경우
    2. ENUM 으로 처리될수 있는건 무조건 ENUM 으로, 대문자+언더바 통일
    3. 파라미터/ENUM 이름 정할때 다른 비슷한 API 들 꼼꼼히 찾아보고 정하기
        - ex) 검색어 파라미터는 searchKeyword 로 통일
        - ex) 사용자 이름 기준으로 검색할시, 검색 유형은 searchType=NAME 로 통일
        - ex) 통합 검색은 searchType=ALL 또는 생략으로 통일
    4. LocalTime, LocalDate, LocalDateTime 등 시간 타입을 응답할떄는 스웨거에서 잘 뜨는지 확인하고 조치하기
        - LocalDateTime 은 기본 형식(ISO-8601)으로 넘겨주면 프론트에서 알아서 파싱하기로 합의됨.
        - LocalTime 은 별도 설정하지 않으면 스웨거에 잘못 표시되므로 주의.
            - [AdminClassroomDetailResponse](/src/main/java/com/backend/server/api/admin/classroom/dto/AdminClassroomDetailResponse.java) 참고
    5. 모든 파라미터나 DTO는 스웨거에서 잘 보이는지 확인해야함.
4. 스웨거 설명 작성 규칙
    1. 조회 API 는 모든 요청 필드에 설명이 있어야함.
    2. DTO 내의 필드에 대한 설명은 DTO 내에 적기
        - 목적 : 수정하거나 복붙하고 관리가 안되는 경우가 많기 떄문.
    3. 요청/응답 필드에 대한 설명을 DTO에 @Schema(description="설명") 이용해서 적기
        - 조회 API를 제외하고 이름/타입/ENUM값 만으로 의도가 충분히 전달된다면 적지 않아도 괜찮음.
    4. 설명에 말 줄임표 "..." 사용하지 않기.