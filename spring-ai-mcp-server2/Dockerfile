FROM public.ecr.aws/docker/library/eclipse-temurin:25-jre-alpine

# Install curl (required if using minimal Alpine images)
RUN apk add --no-cache curl

# Set default values for environment variables
ENV SERVER_PORT="8082"

# Copy application
COPY spring-ai-mcp-server2-0.0.1-SNAPSHOT.jar /var/application/

# Change to working directory
WORKDIR /var/application/

# Define healthcheck to ping Spring Boot health endpoint
HEALTHCHECK --interval=5s \
            --timeout=3s \
            --start-period=30s \
            --retries=5 \
            CMD curl -f http://localhost:8082/spring-ai-mcp-server2/tool-definitions || exit 1

# Expose Ports
EXPOSE $SERVER_PORT

# Run application
CMD java -XX:MaxRAMPercentage=75.0 -XX:+PrintFlagsFinal -jar /var/application/spring-ai-mcp-server2-0.0.1-SNAPSHOT.jar