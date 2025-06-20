FROM public.ecr.aws/docker/library/eclipse-temurin:21-jre-alpine

# Set default values for environment variables
ENV SERVER_PORT="8082"

# Copy application
COPY spring-ai-mcp-server2-0.0.1-SNAPSHOT.jar /var/application/

# Change to working directory
WORKDIR /var/application/

# Expose Ports
EXPOSE $SERVER_PORT

# Run application
CMD java -XX:MaxRAMPercentage=75.0 -XX:+PrintFlagsFinal -jar /var/application/spring-ai-mcp-server2-0.0.1-SNAPSHOT.jar