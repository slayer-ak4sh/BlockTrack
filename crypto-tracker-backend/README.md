# 🚀 Scalable Cryptocurrency Real-time Price Tracker on AWS

A comprehensive, cloud-native cryptocurrency price tracking application built with Spring Boot backend and React frontend, deployed on AWS infrastructure.

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   React Frontend │────│  Spring Boot API │────│   AWS Services  │
│   (Port 3000)    │    │   (Port 8080)    │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │                        │
                                │                        ├── DynamoDB
                                │                        ├── CloudWatch
                                └────────────────────────├── Elastic Beanstalk
                                                         └── CoinGecko API
```

## ✨ Key Features

### 🔄 Real-time Price Monitoring
- **30-second updates** from CoinGecko API
- **Live price animations** with up/down indicators
- **Historical data storage** in DynamoDB
- **Automatic failover** to direct API calls

### 🔔 Smart Alert System
- **Price threshold alerts** for any cryptocurrency
- **Real-time notifications** when thresholds are crossed
- **CloudWatch integration** for monitoring
- **Customizable alert management**

### 📊 Performance Monitoring
- **CloudWatch metrics** for API response times
- **Database performance tracking**
- **Error rate monitoring**
- **Scalability metrics**

### 🌐 Scalable Infrastructure
- **Elastic Beanstalk** auto-scaling
- **DynamoDB** for high-performance data storage
- **CloudWatch** for comprehensive monitoring
- **CORS-enabled** API for frontend integration

## 🛠️ Technology Stack

### Backend
- **Spring Boot 3.2** - REST API framework
- **Java 21** - Programming language
- **AWS SDK** - Cloud services integration
- **DynamoDB** - NoSQL database
- **CloudWatch** - Monitoring and alerts
- **Maven** - Dependency management

### Frontend
- **React 18** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool
- **Tailwind CSS** - Styling
- **Framer Motion** - Animations

### AWS Services
- **Elastic Beanstalk** - Application hosting
- **DynamoDB** - Data storage
- **CloudWatch** - Monitoring
- **IAM** - Security and permissions

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Node.js 18+
- Maven 3.6+
- AWS CLI configured
- AWS Account with appropriate permissions

### 1. Clone and Setup
```bash
cd crypto-tracker-backend
```

### 2. Configure Environment
Update `.env` file with your AWS credentials:
```env
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
AWS_REGION=us-east-1
```

### 3. Run Locally
```bash
# Option 1: Use the startup script
start-app.bat

# Option 2: Manual startup
# Terminal 1 - Backend
cd crypto-tracker-backend
mvn spring-boot:run

# Terminal 2 - Frontend
cd coin-sparkle-chart
npm install
npm run dev
```

### 4. Deploy to AWS
```bash
cd crypto-tracker-backend
deploy-aws.bat
```

## 📋 API Endpoints

### Cryptocurrency Data
- `GET /api/crypto/prices/stored` - Get cached crypto data
- `GET /api/crypto/prices` - Fetch fresh data from CoinGecko
- `POST /api/crypto/refresh` - Manually refresh all prices
- `GET /api/crypto/price/{symbol}` - Get specific cryptocurrency

### Price Alerts
- `POST /api/alerts/price/{symbol}?threshold={amount}` - Set price alert
- `DELETE /api/alerts/price/{symbol}` - Remove price alert
- `GET /api/alerts/price` - Get all active alerts

### Health & Monitoring
- `GET /api/health` - Application health check
- CloudWatch metrics automatically published

## 🎯 Use Cases Implemented

### Scenario 1: Real-Time Price Monitoring and Alerts ✅
- Users can set price alerts for any cryptocurrency
- System monitors prices every 30 seconds
- CloudWatch triggers notifications when thresholds are crossed
- Real-time price updates with visual indicators

### Scenario 2: Historical Data Analysis ✅
- All price data stored in DynamoDB with timestamps
- Historical trend visualization with sparkline charts
- Market cap and volume tracking
- 24h price change analysis

### Scenario 3: Scalability and Performance Monitoring ✅
- Elastic Beanstalk auto-scaling based on traffic
- CloudWatch monitors response times and error rates
- DynamoDB performance metrics tracking
- Real-time application health monitoring

## 📊 Monitoring & Metrics

### CloudWatch Metrics Published
- `ScheduledUpdateCount` - Number of scheduled updates
- `ScheduledUpdateDuration` - Time taken for updates
- `CryptocurrenciesUpdated` - Count of cryptos updated
- `ApiResponseTime` - API call response times
- `DatabaseResponseTime` - Database operation times
- `PriceAlert` - Number of alerts triggered

### Performance Monitoring
- API endpoint response times
- Database query performance
- Error rates and success rates
- Memory and CPU utilization

## 🔧 Configuration

### Application Properties
```yaml
server:
  port: 8080
  servlet:
    context-path: /api

scheduler:
  enabled: true
  fixed-rate: 30000 # 30 seconds

aws:
  region: us-east-1
  
dynamodb:
  table:
    users: CryptoTracker-Users
    market-prices: CryptoTracker-MarketPrices
    watchlist: CryptoTracker-Watchlist
```

### Frontend Configuration
```typescript
// Vite proxy configuration
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
}
```

## 🌐 Access URLs

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/api/health
- **API Documentation**: http://localhost:8080/api/swagger-ui.html

## 🔒 Security Features

- CORS configuration for cross-origin requests
- Input validation and sanitization
- AWS IAM role-based access control
- Secure credential management
- Rate limiting on API endpoints

## 📈 Scalability Features

- **Auto-scaling**: Elastic Beanstalk scales based on demand
- **Caching**: DynamoDB provides fast data access
- **Load balancing**: Built-in with Elastic Beanstalk
- **Monitoring**: CloudWatch alerts for performance issues

## 🚨 Troubleshooting

### Common Issues
1. **DynamoDB Access Denied**: Check AWS credentials and IAM permissions
2. **CORS Errors**: Verify frontend URL in SecurityConfig
3. **API Timeouts**: Check CoinGecko API rate limits
4. **Build Failures**: Ensure Java 21 and Maven are properly installed

### Logs Location
- Application logs: `logs/crypto-tracker.log`
- CloudWatch logs: AWS Console → CloudWatch → Log Groups
- Elastic Beanstalk logs: EB Console → Logs

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Built with ❤️ for the AWS Capstone Project**