# 🚀 CEC 프로젝트 백엔드 레포지토리

이 레포지토리는 **CEC 프로젝트**의 백엔드 소스코드를 관리합니다.  
Spring Boot 프레임워크를 기반으로 다양한 기술 스택을 활용하여 개발되었습니다.

---

## 🛠️ 기술 스택

| 구성 요소         | 사용 기술                                       |
|---------------|---------------------------------------------|
| 🔧 프레임워크      | Spring Boot, JPA                            |
| 🚀 배포 자동화     | AWS EC2, Jenkins                            |
| 📈 모니터링       | Prometheus, Grafana                         |
| 🗄️ 데이터베이스    | PostgreSQL, Redis                           |
| 📂 정적 파일 저장소  | AWS S3                                      |
| 🧪 테스트        | JUnit 5                                     |
| 🌐 웹 서버 / DNS | Nginx, Cloudflare *(리버스 프록시, SSL, 캐시 등 사용)* |

---

## 🧭 시스템 아키텍처

![🧩 시스템 아키텍처](docs/system-arch.png)

---

## 🧮 ERD (Entity-Relationship Diagram)

![📊 ERD](sql/erd/erd.png)

---

## 📚 개발자 참고 문서

[📄 개발 방법 문서 바로가기](docs/dev-docs.md)