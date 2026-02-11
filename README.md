# Crypto Tracker - Full Stack Application

A production-ready cryptocurrency tracking application with real-time price updates, user authentication, and portfolio management.
scalable cryptocurrency real-time price tracker on aws.
## 🏗️ Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0 with Java 21
- **Database**: AWS DynamoDB
- **API**: RESTful API with OpenAPI documentation
- **Security**: Spring Security with BCrypt password hashing
- **External API**: CoinGecko API for cryptocurrency data
- **Monitoring**: Spring Actuator with AWS CloudWatch integration

### Frontend (React + TypeScript)
- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite
- **UI Library**: Radix UI + Tailwind CSS
- **State Management**: React Query + Context API
- **Routing**: React Router DOM
- **Charts**: Recharts for data visualization

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Node.js 18 or higher
- AWS Account (for DynamoDB)
- Maven 3.6+

### 1. Clone and Setup
```bash
git clone <repository-url>
cd AWS Project
```

### 2. Backend Configuration
1. Navigate to `crypto-tracker-backend/crypto-tracker-backend/`
2. Copy `.env.example` to `.env`
3. Update the `.env` file with your AWS credentials:
```env
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_REGION=us-east-1
DYNAMODB_USERS_TABLE=CryptoTracker-Users
DYNAMODB_MARKET_PRICES_TABLE=CryptoTracker-MarketPrices
DYNAMODB_WATCHLIST_TABLE=CryptoTracker-Watchlist
```

### 3. Frontend Configuration
1. Navigate to `crypto-pulse-dashboard/`
2. Install dependencies:
```bash
npm install
```

### 4. Start the Application
Run the startup script:
```bash
start-full-app.bat
```

Or start manually:

**Backend:**
```bash
cd crypto-tracker-backend/crypto-tracker-backend
mvn spring-boot:run
```

**Frontend:**
```bash
cd crypto-pulse-dashboard
npm run dev
```

## 🌐 Application URLs

- **Frontend**: http://localhost:8080
- **Backend API**: http://localhost:8081/api
- **API Documentation**: http://localhost:8081/swagger-ui.html
- **Health Check**: http://localhost:8081/api/health

## 📋 Features

### ✅ Implemented Features
- **User Authentication**: Register, login, logout
- **Real-time Crypto Data**: Live price updates from CoinGecko API
- **Responsive Dashboard**: Modern UI with dark/light theme
- **Data Persistence**: AWS DynamoDB integration
- **API Documentation**: Swagger/OpenAPI integration
- **Error Handling**: Comprehensive error handling and user feedback
- **Security**: CORS configuration, password hashing
- **Monitoring**: Health checks and logging

### 🔄 Real-time Updates
- Automatic price refresh every 30 seconds
- Visual indicators for price changes
- WebSocket-ready architecture for future enhancements

### 🔐 Security Features
- BCrypt password hashing
- CORS protection
- Input validation
- Secure session management

## 🏗️ Production Deployment

### Build for Production
```bash
build-production.bat
```

### Manual Build Process

**Backend:**
```bash
cd crypto-tracker-backend/crypto-tracker-backend
mvn clean package -DskipTests
```

**Frontend:**
```bash
cd crypto-pulse-dashboard
npm run build:prod
```

### Deployment Files
- **Backend JAR**: `crypto-tracker-backend/crypto-tracker-backend/target/crypto-tracker-backend-0.0.1-SNAPSHOT.jar`
- **Frontend Build**: `crypto-pulse-dashboard/dist/`

### Environment Variables for Production
Update `.env.production` in the frontend and backend environment variables:

**Frontend (.env.production):**
```env
VITE_API_URL=https://your-backend-domain.com/api
```

**Backend (Environment Variables):**
```env
AWS_ACCESS_KEY_ID=your-production-access-key
AWS_SECRET_ACCESS_KEY=your-production-secret-key
SERVER_PORT=8081
```

## 🔧 Development

### Backend Development
```bash
cd crypto-tracker-backend/crypto-tracker-backend
mvn spring-boot:run
```

### Frontend Development
```bash
cd crypto-pulse-dashboard
npm run dev
```

### Testing
```bash
# Frontend tests
cd crypto-pulse-dashboard
npm test

# Backend tests
cd crypto-tracker-backend/crypto-tracker-backend
mvn test
```

## 📊 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/check/{username}` - Check username availability

### Cryptocurrency Data
- `GET /api/crypto/prices` - Get current crypto prices
- `GET /api/crypto/prices/stored` - Get stored crypto data
- `GET /api/crypto/price/{symbol}` - Get specific crypto by symbol
- `POST /api/crypto/refresh` - Manually refresh prices

### Health & Monitoring
- `GET /api/health` - Application health check

## 🗄️ Database Schema

### DynamoDB Tables
1. **CryptoTracker-Users**: User authentication data
2. **CryptoTracker-MarketPrices**: Cryptocurrency price data
3. **CryptoTracker-Watchlist**: User watchlists (future feature)

## 🔍 Monitoring & Logging

### Application Logs
- Location: `crypto-tracker-backend/crypto-tracker-backend/logs/`
- Format: Structured logging with timestamps
- Levels: INFO, WARN, ERROR

### Health Monitoring
- Spring Actuator endpoints
- AWS CloudWatch integration
- Real-time connection status

## 🚨 Troubleshooting

### Common Issues

1. **Backend won't start**
   - Check Java version: `java -version`
   - Verify AWS credentials in `.env`
   - Check port 8081 availability

2. **Frontend can't connect to backend**
   - Verify backend is running on port 8081
   - Check CORS configuration
   - Verify API URL in environment variables

3. **Database connection issues**
   - Verify AWS credentials
   - Check DynamoDB table names
   - Ensure proper IAM permissions

### Port Configuration
- Backend: 8081
- Frontend: 8080
- Ensure these ports are available

## 📈 Performance Optimization

### Backend
- Connection pooling for DynamoDB
- Caching for frequently accessed data
- Async processing for external API calls

### Frontend
- Code splitting with Vite
- Lazy loading of components
- Optimized bundle size

## 🔮 Future Enhancements

- [ ] WebSocket integration for real-time updates
- [ ] Portfolio tracking and analytics
- [ ] Price alerts and notifications
- [ ] Advanced charting and technical indicators
- [ ] Mobile app development
- [ ] Multi-language support

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review the API documentation

---

**Built with ❤️ using Spring Boot, React, and AWS**