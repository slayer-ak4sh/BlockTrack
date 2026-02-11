# Crypto Tracker - Frontend & Backend Integration

## Overview
This project integrates a React frontend (coin-sparkle-chart) with a Spring Boot backend (crypto-tracker-backend) for real-time cryptocurrency tracking.

## Architecture
- **Frontend**: React + TypeScript + Vite (Port 3000)
- **Backend**: Spring Boot + Java (Port 8080)
- **Database**: DynamoDB
- **API**: CoinGecko (with backend caching)

## Setup Instructions

### Prerequisites
- Java 17+
- Node.js 18+
- Maven
- AWS credentials (for DynamoDB)

### 1. Backend Setup
```bash
cd crypto-tracker-backend
mvn clean install
mvn spring-boot:run
```

### 2. Frontend Setup
```bash
cd coin-sparkle-chart
npm install
npm run dev
```

### 3. Run Both Services
Option A - Using batch script:
```bash
start-app.bat
```

Option B - Using npm:
```bash
cd coin-sparkle-chart
npm install concurrently
npm run dev:full
```

## Integration Features

### API Endpoints
- `GET /api/crypto/prices/stored` - Get cached crypto data
- `POST /api/crypto/refresh` - Refresh crypto data
- `GET /api/crypto/price/{symbol}` - Get specific crypto data
- `GET /api/health` - Health check

### Frontend Features
- **Smart Data Fetching**: Uses backend first, falls back to CoinGecko
- **Real-time Updates**: 30-second polling with price change animations
- **Proxy Configuration**: Vite proxy routes `/api` to backend
- **Error Handling**: Graceful fallback when backend is unavailable

### CORS Configuration
Backend allows requests from:
- http://localhost:3000 (Frontend)
- http://localhost:8080 (Backend)
- http://127.0.0.1:3000
- http://127.0.0.1:8080

## URLs
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- Health Check: http://localhost:8080/api/health

## Data Flow
1. Frontend requests data from `/api/crypto/prices/stored`
2. If backend unavailable, falls back to CoinGecko API
3. Backend fetches from CoinGecko and caches in DynamoDB
4. Scheduled updates every 30 seconds
5. Manual refresh triggers backend data update

## Environment Variables
Backend (.env):
```
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=your_key
AWS_SECRET_ACCESS_KEY=your_secret
COINGECKO_API_KEY=your_api_key
```

## Troubleshooting
- Ensure backend is running on port 8080
- Check CORS configuration if requests fail
- Verify AWS credentials for DynamoDB access
- Check browser console for API errors