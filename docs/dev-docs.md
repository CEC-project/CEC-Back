## DB, Redis 한꺼번에 실행하는 방법
1. [도커 데스크탑](https://www.docker.com/products/docker-desktop/)을 설치한다 (리눅스 환경에서는 도커만 설치한다)
2. 프로젝트경로/docker/db-redis 로 이동한다
3. docker-compose up -d 으로 실행한다 (컴퓨터를 재시작해도 중지시킬 때까지 꺼지지 않음)
4. docker-compose down 으로 중지한다

## DB, Redis 각각 실행하는 방법
1. [도커 데스크탑](https://www.docker.com/products/docker-desktop/)을 설치한다 (리눅스 환경에서는 도커만 설치한다)
2. 다음 명령어를 입력하면 DB가 실행된다. (컴퓨터를 재시작해도 중지시킬 때까지 꺼지지 않음)
```  
실행 명령어 (윈도우에서는 \ 를 ^ 로 고쳐야 CMD에서 실행됨)
docker run --name postgres-container \
  -e POSTGRES_PASSWORD=1234 \
  -e POSTGRES_USER=cec \
  -e POSTGRES_DB=cec \
  -p 5432:5432 \
  -v postgres-data:/var/lib/postgresql/data \
  --restart unless-stopped \
  -d postgres

중지 명령어
docker stop postgres-container
```
3. 다음 명령어를 입력하면 Redis가 실행된다. (컴퓨터를 재시작해도 중지시킬 때까지 꺼지지 않음)
```  
실행 명령어 (윈도우에서는 \ 를 ^ 로 고쳐야 CMD에서 실행됨)
docker run --name redis-container \
  -p 6379:6379 \
  --restart unless-stopped \
  -d redis
  
중지 명령어
docker stop redis-container
```
- 참고 링크 : https://hub.docker.com/_/redis
- 참고 링크 : https://hub.docker.com/_/postgres

## 서버 실행 방법
1. intelliJ 에서 클론한 프로젝트를 연다
2. settings private 레포지토리에서 설정파일을 다운받아 src/main/resources/settings/ 경로에 저장한다
3. 의존성(라이브러리)이 설치되는지 확인한다 (intelliJ 화면 하단에 뜸)
4. 의존성이 다 설치되면, 실행버튼을 누르면 된다

## 디렉토리/파일 설명
아래 내용은 상황에 따라 유연하게 바꿔도 됨. 
- src.main.java.cec.backend.server
  - config
    - DB, 스프링 등 설정파일이 위치하는 곳
  - security
    - 보안 설정파일이 위치하는 곳
  - model
    - DB에 연관된 소스코드
    - entity
      - 엔티티(테이블 마다 존재함) 이 위치하는 곳
    - repository
      - DB에 날리는 쿼리가 위치하는 곳 (메소드 하나가 sql 쿼리에 대응됨)
  - api
    - user
      - 사용자 웹의 API 가 위치하는 곳
      - controller
        - 컨트롤러가 위치하는 곳 (메소드 하나가 엔드포인트 하나에 대응됨)
      - service
        - 서비스가 위치하는 곳 (메소드 하나가 API의 로직에 대응됨) (컨트롤러에서는 항상 서비스의 메소드를 호출해서 처리함)
      - dto
        - 컨트롤러가 받는 요청과 응답의 JSON 형식을 지정해놓은 파일들
        - DTO 를 만들때는, 이 패키지에 하위 패키지 하나를 만들고, 그 안에 DTO 클래스를 만들면 됨
    - admin
      - 관리자 웹의 API 가 위치하는 곳
      - user 패키지와 같은 구조임
    - common
      - 공통 API, DTO 등이 위치하는 곳
      - user 패키지와 같은 구조임
- src.main.resource.settings
  - 설정파일이 위치하는 곳
  - 설정파일은 백엔드 private 레포지토리에서 받을수 있음
- docs
  - 본 문서와 사진이 저장되는 위치
- sql
  - ddl, dml, erd.dbml 파일이 위치하는 곳
- build.gradle
  - 의존성(라이브러리) 목록
  - 프로젝트에 대한 설정

## 깃/깃허브 관련 규칙 (4/11 19:49)
1. "feat : 작업내용", "fix : 작업내용" 과 같이 커밋 컨벤션 지키기
2. 커밋할때는 가능하면 실행이 가능할때만 커밋하기, 컴파일시 뜨는 경고문구도 최대한 해결해서 커밋하기
3. 설정파일 저장용 private 레포지토리를 업데이트 시켰다면, 팀원들에게 알려서 각자 받아가도록 하기
4. 작업하기 전에, "feature/기능명", "fix/기능명" 처럼 브랜치를 나누고 작업하고, pr을 날릴때 메인 브랜치로 pr 하기 (= GtiHub Flow)

## 코딩 관련 규칙 (4/15 18:30)
1. 가능하면 DTO 안에 "toEntity 메소드" 처럼 엔티티로 바꾸는 메소드나, "[엔티티로 생성하는 생성자](../src/main/java/com/backend/server/api/admin/dto/user/AdminUserResponse.java)" 를 만들고 최대한 사용하기
2. 컨트롤러, 서비스, DTO 파일이 admin 패키지 아래에 있으면 "Admin" 을 덧붙이기. common 패키지 아래에 있으면 "Common" 을 덧붙이기
   - IDE 자동완성 충돌과, 스프링 빈 이름 충돌을 근본적으로 방지하기 위함