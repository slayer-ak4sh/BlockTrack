#!/bin/bash

# Build the application
mvn clean package -DskipTests

# Create Elastic Beanstalk application
eb init crypto-tracker-backend --platform java --region us-east-1

# Create environment (first time only)
eb create crypto-tracker-env --single --instance-types t2.micro

# Deploy
eb deploy

# Open in browser
eb open