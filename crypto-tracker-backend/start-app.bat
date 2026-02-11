@echo off
echo Starting Crypto Tracker Application...
echo.

echo Starting Backend (Spring Boot)...
start "Backend" cmd /k "cd /d crypto-tracker-backend && mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 10 /nobreak > nul

echo Starting Frontend (React + Vite)...
start "Frontend" cmd /k "cd /d coin-sparkle-chart && npm run dev"

echo.
echo Both services are starting...
echo Backend: http://localhost:8080/api
echo Frontend: http://localhost:3000
echo.
pause