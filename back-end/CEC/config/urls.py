from django.contrib import admin
from django.urls import include, path, re_path
from drf_yasg.views import get_schema_view  # get_schema_view 임포트
from drf_yasg import openapi
from rest_framework import permissions

# Swagger 스키마 뷰 정의
schema_view = get_schema_view(
    openapi.Info(
        title="Your Server Name or Swagger Docs name",
        default_version="v1",
        description="Your Swagger Docs descriptions",
        terms_of_service="https://www.google.com/policies/terms/",
        contact=openapi.Contact(name="Support", email="support@example.com"),
        license=openapi.License(name="Test License"),
    ),
    public=True,
    permission_classes=(permissions.AllowAny,),
)

urlpatterns = [
    re_path(r'^swagger(?P<format>\.json|\.yaml)$', schema_view.without_ui(cache_timeout=0), name='schema-json'),
    re_path(r'^swagger/$', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
    re_path(r'^redoc/$', schema_view.with_ui('redoc', cache_timeout=0), name='schema-redoc'),
    path('admin/', admin.site.urls),
    path('api/', include("api.urls")),
]
