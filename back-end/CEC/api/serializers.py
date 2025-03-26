from rest_framework import serializers
from .models import User, YourModel

# serializers.py - 모델 데이터를 JSON 형식으로 변환하는 파일

class YourModelSerializer(serializers.ModelSerializer):
    """
    YourModelSerializer 클래스는 YourModel 모델의 데이터를 JSON으로 변환하는 역할
    """
    class Meta:
        model = YourModel  # 직렬화할 모델 지정
        fields = '__all__'  # 모든 필드를 포함

class UserSerializer(serializers.ModelSerializer):
    """
    UserSerializer는 User 모델의 데이터를 JSON으로 변환하고,
    입력 데이터의 유효성을 검사
    """
    class Meta:
        model = User  # 사용할 모델 지정
        fields = '__all__'  # 모든 필드를 포함
