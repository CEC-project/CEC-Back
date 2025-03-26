from django.db import models

# models.py - 데이터베이스 테이블을 정의하는 파일

class YourModel(models.Model):
    """
    YourModel 클래스는 데이터베이스의 YourModel 테이블을 나타냄
    Django의 ORM을 사용하여 테이블을 정의
    """
    name = models.CharField(max_length=100)  # 문자열 필드
    description = models.TextField()  # 긴 텍스트 필드
    created_at = models.DateTimeField(auto_now_add=True)  # 생성 시간 자동 저장

    def __str__(self):
        return self.name  # 객체를 출력할 때 name 필드 값 반환

# 예시
class User(models.Model):
    """
    User 모델은 회원의 id, pwd, username을 저장
    """
    id = models.AutoField(primary_key=True)  # 자동 증가 ID
    pwd = models.CharField(max_length=255)  # 비밀번호 (추후 해싱 필요)
    username = models.CharField(max_length=100, unique=True)  # 중복 방지

    def __str__(self):
        return self.username  # 객체를 출력할 때 username 반환
