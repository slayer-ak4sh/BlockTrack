#!/bin/bash
# Fixed Deploy Script for EC2 with IAM Role

echo "=== Stopping existing containers ==="
docker-compose -f docker-compose-ec2.yml down 2>/dev/null || true

echo "=== Building backend ==="
cd crypto-tracker-backend/crypto-tracker-backend
docker build -t crypto-backend . || exit 1
cd ../..

echo "=== Building frontend ==="
cd crypto-pulse-dashboard
docker build -t crypto-frontend . || exit 1
cd ..

echo "=== Starting containers ==="
docker run -d --name crypto-tracker-backend \
  -p 8081:8081 \
  -e AWS_REGION=us-east-1 \
  -e DYNAMODB_USERS_TABLE=Users \
  -e DYNAMODB_MARKET_PRICES_TABLE=MarketPrices \
  -e DYNAMODB_WATCHLIST_TABLE=Watchlist \
  -e SNS_TOPIC_ARN=arn:aws:sns:us-east-1:149536455348:crypto-topic \
  --restart unless-stopped \
  crypto-backend

echo "=== Waiting for backend to start ==="
sleep 15

echo "=== Starting frontend ==="
docker run -d --name crypto-tracker-frontend \
  -p 8080:8080 \
  --link crypto-tracker-backend:backend \
  --restart unless-stopped \
  crypto-frontend

echo "=== Checking status ==="
docker ps

PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

echo ""
echo "✅ Deployment Complete!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Frontend: http://$PUBLIC_IP:8080"
echo "Backend:  http://$PUBLIC_IP:8081/api/health"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "View backend logs:  docker logs -f crypto-tracker-backend"
echo "View frontend logs: docker logs -f crypto-tracker-frontend"
