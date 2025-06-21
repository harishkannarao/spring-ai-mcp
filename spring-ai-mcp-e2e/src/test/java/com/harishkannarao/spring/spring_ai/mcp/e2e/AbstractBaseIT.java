package com.harishkannarao.spring.spring_ai.mcp.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public abstract class AbstractBaseIT {

	private final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

	protected RequestSpecification mcpAgentRestClient() {
		return RestAssured.given()
			.filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
			.baseUri("http://localhost:8080/spring-ai-mcp-client");
	}
	protected RequestSpecification mcpServer1RestClient() {
		return RestAssured.given()
			.filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
			.baseUri("http://localhost:8081/spring-ai-mcp-server1");
	}
	protected RequestSpecification mcpServer2RestClient() {
		return RestAssured.given()
			.filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
			.baseUri("http://localhost:8082/spring-ai-mcp-server2");
	}

	protected JsonNode toJsonNode(String json) {
		try {
			return objectMapper.readTree(json);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
