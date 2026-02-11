@echo off
echo ========================================
echo FIXING ZERO PRICES ISSUE
echo ========================================
echo.
echo Stopping any running Java processes...
taskkill /F /IM java.exe 2>nul

echo.
echo Cleaning and rebuilding backend...
cd crypto-tracker-backend\crypto-tracker-backend

call mvn clean
call mvn compile

echo.
echo ========================================
echo Starting Backend with Fixed Data Format
echo ========================================
echo.
echo Backend will start on: http://localhost:8081/api
echo.
echo Wait for: "Started CryptoTrackerBackendApplication"
echo Then open: http://localhost:8080
echo.
echo You should now see REAL prices, not $0.0000!
echo ========================================
echo.

call mvn spring-boot:run
