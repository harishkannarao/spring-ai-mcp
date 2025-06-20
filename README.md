# Spring AI MCP
This repository is a playground for learning and experimenting new ideas in MCP using Spring AI

### Github Actions Build status
[![Build Status](https://github.com/harishkannarao/spring-ai-mcp/workflows/CI-main/badge.svg)](https://github.com/harishkannarao/spring-ai-mcp/actions?query=workflow%3ACI-main)

### Required Software and Tools
* Java Version: 21 (Execute **_java -version_** in command line after installation)
* Ollama: latest (Execute **ollama -v** in command line after installation)
* Integrated Development Environment: Any version IntelliJ Idea or Eclipse

### Pull Ollama Models

    ollama pull llama3.2:3b

### Build command

    ./mvnw clean install

### Skip all tests and generate artefacts

    ./mvnw clean install -DskipTests

### Run MCP Servers and Client applications

    ./mvnw clean spring-boot:run -pl spring-ai-mcp-server1

    ./mvnw clean spring-boot:run -pl spring-ai-mcp-server2

    ./mvnw clean spring-boot:run -pl spring-ai-mcp-client

or to run the client against OpenAI

    export OPEN_AI_KEY={open_ai_key}

    ./mvnw clean -Dspring-boot.run.profiles=openai spring-boot:run -pl spring-ai-mcp-client

or to run the client against AWS LLM Model

    export AWS_AI_ACCESS_KEY_ID={aws_key_id}

    export AWS_AI_SECRET_ACCESS_KEY={aws_secret_key}

    ./mvnw clean -Dspring-boot.run.profiles=aws spring-boot:run -pl spring-ai-mcp-client

### Run MCP Servers and Client applications using docker compose

    export SPRING_PROFILES_ACTIVE="aws"

or

    export SPRING_PROFILES_ACTIVE="openai"

    ./docker-compose-run.sh

### Run E2E test with MCP Host and MCP Servers

    ./mvnw clean install -DskipE2E=false -pl spring-ai-mcp-e2e