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

curl --retry 120 --retry-delay 1 --retry-all-errors "http://localhost:8081/spring-ai-mcp-server1/tool-definitions"

curl --retry 120 --retry-delay 1 --retry-all-errors "http://localhost:8082/spring-ai-mcp-server2/tool-definitions"

curl --retry 120 --retry-delay 1 --retry-all-errors "http://localhost:8080/spring-ai-mcp-client/tool-definitions"