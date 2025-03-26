from django.contrib import admin
from .models import User, YourModel

# admin.py - Django 관리자 페이지에서 모델을 관리할 수 있도록 설정하는 파일

@admin.register(YourModel)
class YourModelAdmin(admin.ModelAdmin):
    """
    Django 관리자 페이지에서 YourModel 데이터를 관리할 수 있도록 설정
    """
    list_display = ('id', 'name', 'created_at')  # 목록에 표시할 필드
    search_fields = ('name',)  # 검색 가능 필드

admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = ('id', 'username', 'pwd')  # 목록에서 보일 필드 지정
    search_fields = ('username',)  # 검색 기능 추가
    ordering = ('id',)  # 정렬 기준 설정
