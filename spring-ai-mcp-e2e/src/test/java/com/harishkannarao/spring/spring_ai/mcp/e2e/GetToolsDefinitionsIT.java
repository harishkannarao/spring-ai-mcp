package com.harishkannarao.spring.spring_ai.mcp.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class GetToolsDefinitionsIT extends AbstractBaseIT {

	@Test
	public void verify_McpServer1_toolsDefinition() {
		Response response = mcpServer1RestClient()
			.accept(ContentType.JSON)
			.get("/tool-definitions")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);

		JsonNode jsonNode = toJsonNode(response.body().asString());
		assertThat(jsonNode.isArray()).isTrue();
		List<JsonNode> elements = StreamSupport.stream(jsonNode.spliterator(), false).toList();
		assertThat(elements)
			.anySatisfy(element -> {
				assertThat(element.get("name").asText()).isEqualTo("ticketBookingService");
				assertThat(element.get("description").asText()).containsIgnoringWhitespaces("""
					Book tickets for a movie.
					Input parameters are movie name and total count of tickets to book
					""");
			})
			.anySatisfy(element -> {
				assertThat(element.get("name").asText()).isEqualTo("ticketInventoryService");
				assertThat(element.get("description").asText()).containsIgnoringWhitespaces("""
					Get the available ticket count by movie name
					""");
			});
	}

	@Test
	public void verify_McpServer2_toolsDefinition() {
		Response response = mcpServer2RestClient()
			.accept(ContentType.JSON)
			.get("/tool-definitions")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);

		JsonNode jsonNode = toJsonNode(response.body().asString());
		assertThat(jsonNode.isArray()).isTrue();
		List<JsonNode> elements = StreamSupport.stream(jsonNode.spliterator(), false).toList();
		assertThat(elements)
			.anySatisfy(element -> {
				assertThat(element.get("name").asText()).isEqualTo("weatherService");
				assertThat(element.get("description").asText()).containsIgnoringWhitespaces("""
					Get the weather for the given location or village or town or city.
					The getWeatherRequest takes location along with unit in Celsius or Fahrenheit.
					""");
			});
	}
}
