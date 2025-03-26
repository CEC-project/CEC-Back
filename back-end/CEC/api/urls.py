from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UserCreateView, YourModelViewSet

# urls.py - API 엔드포인트를 설정하는 파일

router = DefaultRouter()
router.register(r'yourmodel', YourModelViewSet)  # /yourmodel/ 엔드포인트 생성

urlpatterns = [
    path('', include(router.urls)),  # 등록한 라우터를 포함
    path('user/create/', UserCreateView.as_view(), name='user-create') # 회원가입 api, 주소,뷰,넘겨줄 이름 설정
]
