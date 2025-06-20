#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

./mvnw clean install -DskipTests

(cd spring-ai-mcp-client && docker build --pull -t com.harishkannarao/spring-ai-mcp-client:latest -f Dockerfile target)

(cd spring-ai-mcp-server1 && docker build --pull -t com.harishkannarao/spring-ai-mcp-server1:latest -f Dockerfile target)

(cd spring-ai-mcp-server2 && docker build --pull -t com.harishkannarao/spring-ai-mcp-server2:latest -f Dockerfile target)

docker compose -f docker-compose.yml down -v

docker compose -f docker-compose.yml up --build -d