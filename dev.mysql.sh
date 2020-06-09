#!/bin/bash
# Create local mysql container for testing

docker run -d -e MYSQL_ALLOW_EMPTY_PASSWORD="yes" -e MYSQL_DATABASE=flyak -p 3306:3306 mysql:5.7
docker run -d -p 6379:6379 redis:6-alpine