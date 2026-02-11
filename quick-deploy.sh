#!/bin/bash
# Quick Deploy Script for EC2 with IAM Role

echo "=== Crypto Tracker Quick Deploy ==="

# Install dependencies if needed
if ! command -v docker &> /dev/null; then
    echo "Installing Docker..."
    sudo yum update -y
    sudo yum install -y docker
    sudo service docker start
    sudo usermod -a -G docker $USER
fi

if ! command -v docker-compose &> /dev/null; then
    echo "Installing Docker Compose..."
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.5/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi

# Create .env if not exists
if [ ! -f .env ]; then
    echo "Creating .env file..."
    cat > .env << EOF
AWS_REGION=us-east-1
DYNAMODB_USERS_TABLE=CryptoTracker-Users
DYNAMODB_MARKET_PRICES_TABLE=CryptoTracker-MarketPrices
DYNAMODB_WATCHLIST_TABLE=CryptoTracker-Watchlist
EOF
    echo "⚠️  Please edit .env and add SNS_TOPIC_ARN if needed"
fi

# Deploy
echo "=== Building and starting containers ==="
docker-compose down
docker-compose up -d --build

echo "=== Waiting for services to start ==="
sleep 10

# Get EC2 public IP
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

echo ""
echo "✅ Deployment Complete!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Frontend: http://$PUBLIC_IP:8080"
echo "Backend:  http://$PUBLIC_IP:8081/api/health"
echo "API Docs: http://$PUBLIC_IP:8081/swagger-ui.html"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "View logs: docker-compose logs -f"
echo "Check status: docker-compose ps"
