#!/bin/bash

cd "$(dirname "$(readlink -f "$0")")" || exit

CERT_DIR="./certs"
WEB_ROOT="./certs/certbot"
NGINX_CONTAINER_NAME="reverse-proxy"

if [ ! -f "$CERT_DIR/live/api.bmvcec.store/fullchain.pem" ]; then
  echo "🔐 인증서가 없어서 새로 생성합니다..."

  # 사용할 디렉토리 생성
  mkdir -p $CERT_DIR/live/api.bmvcec.store/
  mkdir -p $WEB_ROOT/.well-known/acme-challenge/

  echo "이메일을 입력하세요"
  read -r EMAIL

  # 인증서 생성
  docker-compose -f docker-compose-first.yml up -d
  docker run -v "$WEB_ROOT:/var/www/certbot" \
             -v "$CERT_DIR:/etc/letsencrypt" \
             certbot/certbot certonly \
             --webroot -w /var/www/certbot \
             -d "api.bmvcec.store" \
             -d "dev.api.bmvcec.store" \
             -d "jenkins.api.bmvcec.store" \
             -m "$EMAIL" \
             --non-interactive --agree-tos --no-eff-email
  docker-compose -f docker-compose-first.yml down

  # nginx 시작
  docker-compose up -d
else
  docker run --rm \
    -v "$CERT_DIR:/etc/letsencrypt" \
    -v "$WEB_ROOT:/var/www/certbot" \
    certbot/certbot renew

  # nginx 리로드
  docker exec "$NGINX_CONTAINER_NAME" nginx -s reload || echo '⚠️ Nginx 재시작 실패 (컨테이너 실행 중인지 확인하세요)'
fi

############## 인증서 갱신 crontab 등록

SCRIPT_PATH=$(readlink -f "$0")
LOG_FILE="/var/log/certbot-renew.log"

CRON_JOB="0 3 * * * $SCRIPT_PATH >> $LOG_FILE 2>&1"

# 크론탭에 이미 등록되어 있는지 확인
crontab -l 2>/dev/null | grep -F "$SCRIPT_PATH" >/dev/null

if [ $? -ne 0 ]; then
  echo "➕ 크론탭에 등록 중..."
  (crontab -l 2>/dev/null; echo "$CRON_JOB") | crontab -
  echo "✅ 등록 완료: $CRON_JOB"
fi