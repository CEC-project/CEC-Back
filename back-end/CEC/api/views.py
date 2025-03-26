from rest_framework import viewsets, generics
from .models import User, YourModel
from .serializers import UserSerializer, YourModelSerializer

# views.py - 클라이언트의 요청을 처리하는 파일

class YourModelViewSet(viewsets.ModelViewSet):
    """
    YourModelViewSet 클래스는 YourModel 모델의 CRUD API를 자동으로 생성
    DRF의 ModelViewSet을 사용하여 기본적인 기능을 제공
    """
    queryset = YourModel.objects.all()  # 모든 데이터 가져오기
    serializer_class = YourModelSerializer  # 사용할 시리얼라이저 지정

class UserCreateView(generics.CreateAPIView):
    """
    UserCreateView는 회원 정보를 생성하는 API입니다.
    """
    queryset = User.objects.all()  # User 모델 데이터 조회
    serializer_class = UserSerializer  # 직렬화 클래스 지정