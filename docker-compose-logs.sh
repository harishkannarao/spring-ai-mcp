#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

docker compose -f docker-compose.yml logs --tail all --no-color --follow --since 0s