@echo off
echo ========================================
echo   AWS Crypto Tracker Deployment Script
echo ========================================
echo.

echo Step 1: Building the application...
call mvn clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ERROR: Maven build failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Creating DynamoDB tables...
aws dynamodb create-table ^
    --table-name CryptoTracker-Users ^
    --attribute-definitions AttributeName=username,AttributeType=S ^
    --key-schema AttributeName=username,KeyType=HASH ^
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 ^
    --region us-east-1 2>nul

aws dynamodb create-table ^
    --table-name CryptoTracker-MarketPrices ^
    --attribute-definitions AttributeName=coinId,AttributeType=S AttributeName=timestamp,AttributeType=S ^
    --key-schema AttributeName=coinId,KeyType=HASH AttributeName=timestamp,KeyType=RANGE ^
    --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=10 ^
    --region us-east-1 2>nul

aws dynamodb create-table ^
    --table-name CryptoTracker-Watchlist ^
    --attribute-definitions AttributeName=username,AttributeType=S AttributeName=coinId,AttributeType=S ^
    --key-schema AttributeName=username,KeyType=HASH AttributeName=coinId,KeyType=RANGE ^
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 ^
    --region us-east-1 2>nul

echo DynamoDB tables creation initiated...

echo.
echo Step 3: Deploying to Elastic Beanstalk...
eb deploy

if %ERRORLEVEL% neq 0 (
    echo ERROR: Deployment failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Deployment completed successfully!
echo ========================================
echo.
echo Your application should be available at:
eb status | findstr "CNAME"
echo.
echo CloudWatch metrics will be available in AWS Console
echo DynamoDB tables: CryptoTracker-Users, CryptoTracker-MarketPrices, CryptoTracker-Watchlist
echo.
pause