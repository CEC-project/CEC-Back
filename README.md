# 🌟 CEC-Backend 학생들이 사용할 어플리케이션

## 🚀 개발 환경  
- Python 3.10.x  
- Django 4.x  
- 데이터베이스: PostgreSQL (또는 원하는 DB)  
- 기타: Docker, Git, VSCode (추천 IDE)

## ✅ 기술 스택  
- **Backend**: Python, Django  
- **Database**: SQLite
- **배포/서버**: AWS, Docker 

## 📁 네이밍 규칙  
- **파일 및 디렉토리**: 소문자와 언더스코어(_) 사용, 예: `my_app`, `my_model.py`  
- **모델 이름**: 카멜케이스(CamelCase), 예: `MyModel`  
- **변수 및 함수 이름**: 소문자와 언더스코어 사용, 예: `get_user_data()`  
- **클래스 이름**: 카멜케이스(CamelCase), 예: `MyClass`  
- **장고 앱 이름**: 소문자와 언더스코어 사용, 예: `my_app`

## 🧑‍💻 Member  
- 이선욱 - [GitHub](https://github.com/leesunuk)  
- 김용연 - [GitHub](https://github.com/Rider96)  
- 조경준 - [GitHub](https://github.com/GenTaram)  
- 이상민 - [GitHub](https://github.com/geniusBrainLsm)  
- 김진영 - [GitHub](https://github.com/Kimhasa)

## 개발용 DB, Redis 실행 방법  (docker compose 로 간편화 예정)
1. 도커를 설치한다
2. 다음 명령어를 입력하면 DB가 실행된다. (현재 이 명령어는 도커를 재시작할때마다 데이터가 날아감에 주의)
```  
docker run --name postgres-container\
  -e POSTGRES_PASSWORD=1234\
  -e POSTGRES_USER=cec\
  -e POSTGRES_DB=cec\
  -p 5432:5432\
  -d postgres
```
3. 다음 명령어를 입력하면 Redis가 실행된다.
```  
docker run --name redis-container\
  -p 6379:6379\
  -d redis
```
- 참고 링크 : https://hub.docker.com/_/redis
- 참고 링크 : https://hub.docker.com/_/postgres

## 실행 방법
1. 깃 레포지토리를 자기 계정으로 포크한다
2. 포크한 레포지토리를 클론한다
3. intelliJ 에서 클론한 프로젝트를 연다
4. 의존성(라이브러리)이 설치되는지 확인한다 (intelliJ 화면 하단에 뜸)
5. 의존성이 다 설치되면 실행한다