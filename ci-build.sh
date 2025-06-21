#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

./mvnw clean install --batch-mode

./docker-compose-run.sh

./mvnw clean install -DskipE2E=false -pl spring-ai-mcp-e2e