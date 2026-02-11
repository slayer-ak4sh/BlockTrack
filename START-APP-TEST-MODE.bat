@echo off
echo ========================================
echo Starting Crypto Tracker (TEST MODE)
echo ========================================
echo.
echo This will start:
echo 1. Backend on http://localhost:8081/api
echo 2. Frontend on http://localhost:8080
echo.
echo Press Ctrl+C in each window to stop
echo ========================================
echo.

REM Start backend in new window
start "Crypto Tracker Backend" cmd /k "cd crypto-tracker-backend\crypto-tracker-backend && mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 15 /nobreak

REM Start frontend in new window
start "Crypto Tracker Frontend" cmd /k "cd crypto-pulse-dashboard && npm run dev"

echo.
echo ========================================
echo Both services are starting!
echo ========================================
echo.
echo Backend: http://localhost:8081/api
echo Frontend: http://localhost:8080
echo.
echo Wait 30 seconds, then open: http://localhost:8080
echo.
echo You will see the LOGIN page first.
echo Click "Sign up" to create an account!
echo ========================================
