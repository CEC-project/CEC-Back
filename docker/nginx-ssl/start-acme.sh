#!/bin/bash

################# 인증서 생성/갱신 프로그램 설치

# acme.sh 설치하기
if ! command -v acme.sh > /dev/null 2>&1; then
  curl https://get.acme.sh | sh
  echo "export PATH=\"$HOME/.acme.sh:$PATH\"" >> ~/.bashrc
  source ~/.bashrc
  acme.sh --set-default-ca --server zerossl
  echo "이메일을 입력하세요"
  read -r EMAIL
  acme.sh --register-account -m $EMAIL
fi

################# 인증서 생성/갱신 및 nginx 시작/재시작

CERT_DIR="./certs"
WEB_ROOT="./certs/certbot"
NGINX_CONTAINER_NAME="reverse-proxy"

if [ ! -f "$CERT_DIR/fullchain.pem" ]; then
  echo "🔐 인증서가 없어서 새로 생성합니다..."

  # 사용할 디렉토리 생성
  mkdir -p $CERT_DIR
  mkdir -p $WEB_ROOT/.well-known/acme-challenge/

  # 인증서 생성
  docker-compose -f docker-compose-first.yml up -d
  acme.sh --issue -d api.bmvcec.store -d dev.api.bmvcec.store -d jenkins.api.bmvcec.store \
   --webroot "$(readlink -f $WEB_ROOT)" --log
  acme.sh --install-cert -d api.bmvcec.store \
    --key-file       "$(readlink -f $CERT_DIR)/privkey.pem"  \
    --fullchain-file "$(readlink -f $CERT_DIR)/fullchain.pem" \
    --reloadcmd     "docker exec \"$NGINX_CONTAINER_NAME\" nginx -s reload || echo '⚠️ Nginx 재시작 실패 (컨테이너 실행 중인지 확인하세요)'"
  docker-compose -f docker-compose-first.yml down

  # nginx 실행
  docker-compose up -d
else
  echo "🔐 이미 인증서가 존재 하므로, 종료합니다."
  echo "인증서 갱신 및 nginx 리로드는 자동으로 이루어 집니다."
fi