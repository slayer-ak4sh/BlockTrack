@echo off
echo ========================================
echo Rebuilding Backend with Test Mode
echo ========================================
echo.

cd crypto-tracker-backend\crypto-tracker-backend

echo Cleaning previous build...
call mvn clean

echo.
echo Building project...
call mvn compile

echo.
echo Starting backend server on port 8081...
echo Backend will be available at: http://localhost:8081/api
echo.
echo Test endpoints:
echo - POST http://localhost:8081/api/test/auth/register
echo - POST http://localhost:8081/api/test/auth/login
echo - GET  http://localhost:8081/api/test/crypto/prices
echo.

call mvn spring-boot:run
