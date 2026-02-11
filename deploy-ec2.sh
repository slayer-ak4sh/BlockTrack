#!/bin/bash
# Deploy Application on EC2

echo "=== Stopping existing containers ==="
docker-compose down

echo "=== Pulling latest code ==="
git pull origin main

echo "=== Building and starting containers ==="
docker-compose up -d --build

echo "=== Checking container status ==="
docker-compose ps

echo "=== Deployment Complete ==="
echo "Frontend: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
echo "Backend: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8081/api/health"
