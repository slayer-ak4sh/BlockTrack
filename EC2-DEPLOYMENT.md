# EC2 Deployment Guide - Crypto Tracker

## Prerequisites
- AWS Account
- EC2 instance (t3.medium or larger recommended)
- Security Group configured
- DynamoDB tables created

## Step 1: Launch EC2 Instance

### 1.1 Create EC2 Instance
```
AMI: Amazon Linux 2023 or Amazon Linux 2
Instance Type: t3.medium (minimum)
Storage: 20 GB
```

### 1.2 Configure Security Group
Add inbound rules:
```
Type            Port    Source          Description
SSH             22      Your IP         SSH access
HTTP            80      0.0.0.0/0       Frontend
Custom TCP      8080    0.0.0.0/0       Frontend
Custom TCP      8081    0.0.0.0/0       Backend API
```

### 1.3 Create IAM Role for EC2
Create role with policies:
- AmazonDynamoDBFullAccess
- AmazonEC2ContainerRegistryReadOnly (if using ECR)

Attach role to EC2 instance.

## Step 2: Connect to EC2

```bash
ssh -i your-key.pem ec2-user@your-ec2-public-ip
```

## Step 3: Initial Setup on EC2

```bash
# Clone repository
git clone <your-repo-url> crypto-tracker
cd crypto-tracker

# Make scripts executable
chmod +x ec2-setup.sh deploy-ec2.sh

# Run setup script
./ec2-setup.sh

# Log out and log back in
exit
ssh -i your-key.pem ec2-user@your-ec2-public-ip
cd crypto-tracker
```

## Step 4: Configure Environment Variables

```bash
# Create .env file
nano .env
```

Add your AWS credentials:
```env
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_REGION=us-east-1
DYNAMODB_USERS_TABLE=CryptoTracker-Users
DYNAMODB_MARKET_PRICES_TABLE=CryptoTracker-MarketPrices
DYNAMODB_WATCHLIST_TABLE=CryptoTracker-Watchlist
```

Save and exit (Ctrl+X, Y, Enter)

## Step 5: Deploy Application

```bash
./deploy-ec2.sh
```

## Step 6: Enable Auto-Start on Reboot (Optional)

```bash
# Copy service file
sudo cp crypto-tracker.service /etc/systemd/system/

# Enable service
sudo systemctl enable crypto-tracker.service
sudo systemctl start crypto-tracker.service

# Check status
sudo systemctl status crypto-tracker.service
```

## Step 7: Access Application

```
Frontend: http://YOUR_EC2_PUBLIC_IP:8080
Backend API: http://YOUR_EC2_PUBLIC_IP:8081/api/health
API Docs: http://YOUR_EC2_PUBLIC_IP:8081/swagger-ui.html
```

## Useful Commands

### View Logs
```bash
# All logs
docker-compose logs

# Backend logs
docker-compose logs backend

# Frontend logs
docker-compose logs frontend

# Follow logs
docker-compose logs -f
```

### Restart Application
```bash
docker-compose restart
```

### Stop Application
```bash
docker-compose down
```

### Update Application
```bash
git pull origin main
docker-compose up -d --build
```

### Check Container Status
```bash
docker-compose ps
docker ps
```

### Remove All Containers and Images
```bash
docker-compose down --rmi all
```

## Setup with Nginx Reverse Proxy (Production)

### Install Nginx
```bash
sudo yum install -y nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

### Configure Nginx
```bash
sudo nano /etc/nginx/conf.d/crypto-tracker.conf
```

Add:
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }

    location /api {
        proxy_pass http://localhost:8081;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

```bash
sudo nginx -t
sudo systemctl restart nginx
```

Update Security Group: Allow port 80, remove 8080 and 8081

## Setup SSL with Let's Encrypt (Optional)

```bash
sudo yum install -y certbot python3-certbot-nginx
sudo certbot --nginx -d your-domain.com
```

## Monitoring

### Check Disk Space
```bash
df -h
```

### Check Memory
```bash
free -m
```

### Check Docker Stats
```bash
docker stats
```

## Troubleshooting

### Backend won't start
```bash
# Check logs
docker-compose logs backend

# Verify AWS credentials
docker-compose exec backend env | grep AWS

# Test DynamoDB connection
aws dynamodb list-tables --region us-east-1
```

### Frontend can't connect to backend
```bash
# Check if backend is running
curl http://localhost:8081/api/health

# Check nginx config
docker-compose exec frontend cat /etc/nginx/conf.d/default.conf
```

### Port already in use
```bash
# Find process using port
sudo lsof -i :8080
sudo lsof -i :8081

# Kill process
sudo kill -9 <PID>
```

### Out of disk space
```bash
# Clean Docker
docker system prune -a

# Remove old images
docker image prune -a
```

## Backup and Restore

### Backup
```bash
# Backup .env file
cp .env .env.backup

# Export Docker images
docker save crypto-tracker-backend > backend.tar
docker save crypto-tracker-frontend > frontend.tar
```

### Restore
```bash
# Import Docker images
docker load < backend.tar
docker load < frontend.tar
```

## Cost Optimization

- Use t3.small for development (cheaper)
- Use t3.medium for production
- Stop instance when not in use
- Use Elastic IP to maintain same IP address
- Enable CloudWatch alarms for cost monitoring

## Security Best Practices

1. Never commit .env file to Git
2. Use IAM roles instead of access keys when possible
3. Restrict Security Group to specific IPs
4. Enable CloudWatch logging
5. Regular security updates: `sudo yum update -y`
6. Use SSL/TLS in production
7. Implement rate limiting
8. Regular backups

---

**Need Help?**
- Check logs: `docker-compose logs`
- Check container status: `docker-compose ps`
- Restart: `docker-compose restart`
