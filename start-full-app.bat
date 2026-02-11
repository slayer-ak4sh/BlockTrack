@echo off
echo ========================================
echo    Crypto Tracker - Full Stack Startup
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 or higher
    pause
    exit /b 1
)

REM Check if Node.js is installed
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Node.js is not installed or not in PATH
    echo Please install Node.js 18 or higher
    pause
    exit /b 1
)

echo Starting Crypto Tracker Application...
echo.

REM Start Backend
echo [1/2] Starting Backend Server...
cd /d "%~dp0crypto-tracker-backend\crypto-tracker-backend"
start "Crypto Tracker Backend" cmd /k "echo Starting Spring Boot Backend... && mvn spring-boot:run"

REM Wait a bit for backend to start
echo Waiting for backend to initialize...
timeout /t 10 /nobreak >nul

REM Start Frontend
echo [2/2] Starting Frontend Development Server...
cd /d "%~dp0crypto-pulse-dashboard"

REM Install dependencies if node_modules doesn't exist
if not exist "node_modules" (
    echo Installing frontend dependencies...
    npm install
)

start "Crypto Tracker Frontend" cmd /k "echo Starting React Development Server... && npm run dev"

echo.
echo ========================================
echo    Application Started Successfully!
echo ========================================
echo.
echo Backend:  http://localhost:8081/api
echo Frontend: http://localhost:8080
echo.
echo Both servers are running in separate windows.
echo Close those windows to stop the servers.
echo.
pause