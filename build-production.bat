@echo off
echo ========================================
echo    Crypto Tracker - Production Build
echo ========================================
echo.

REM Build Backend
echo [1/2] Building Backend...
cd /d "%~dp0crypto-tracker-backend\crypto-tracker-backend"
echo Cleaning and building Spring Boot application...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Backend build failed
    pause
    exit /b 1
)
echo Backend build completed successfully!
echo.

REM Build Frontend
echo [2/2] Building Frontend...
cd /d "%~dp0crypto-pulse-dashboard"

REM Install dependencies
echo Installing dependencies...
call npm install
if %errorlevel% neq 0 (
    echo ERROR: Failed to install frontend dependencies
    pause
    exit /b 1
)

REM Build for production
echo Building for production...
call npm run build
if %errorlevel% neq 0 (
    echo ERROR: Frontend build failed
    pause
    exit /b 1
)
echo Frontend build completed successfully!
echo.

echo ========================================
echo    Production Build Completed!
echo ========================================
echo.
echo Backend JAR: crypto-tracker-backend\crypto-tracker-backend\target\crypto-tracker-backend-0.0.1-SNAPSHOT.jar
echo Frontend Build: crypto-pulse-dashboard\dist\
echo.
echo To deploy:
echo 1. Deploy the JAR file to your server
echo 2. Deploy the dist folder contents to your web server
echo 3. Update environment variables for production
echo.
pause