# Quick Start Guide

## Issues Fixed:
1. ✅ Backend port changed from 8082 to 8081
2. ✅ API endpoints corrected (removed /test prefix)
3. ✅ Added authentication routing - users must login/signup first
4. ✅ Connection status will now show "Connected" when backend is running

## Start the Application:

### Option 1: Use the startup script
```bash
start-full-app.bat
```

### Option 2: Manual start

**Terminal 1 - Backend:**
```bash
cd crypto-tracker-backend\crypto-tracker-backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd crypto-pulse-dashboard
npm run dev
```

## Access the Application:
- Frontend: http://localhost:8080
- Backend API: http://localhost:8081/api
- First time users will see the Login page
- Click "Sign up" to create an account

## Troubleshooting:
- Make sure port 8081 and 8080 are not in use
- Backend must be running before frontend can connect
- Check backend logs for any AWS credential issues
