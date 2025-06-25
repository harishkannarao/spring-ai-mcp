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

	@Test
	public void verify_McpAgent_localToolsDefinition() {
		Response response = mcpAgentRestClient()
			.accept(ContentType.JSON)
			.get("/tool-definitions")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);

		JsonNode jsonNode = toJsonNode(response.body().asString());
		assertThat(jsonNode.isArray()).isTrue();
		List<JsonNode> elements = StreamSupport.stream(jsonNode.spliterator(), false).toList();
		assertThat(elements)
			.anySatisfy(element -> {
				assertThat(element.get("name").asText()).isEqualTo("goodBookSuggestionService");
				assertThat(element.get("description").asText()).containsIgnoringWhitespaces("""
					Get suggestion of good books for a given genre.
					Also set the limit of books titles to return""");
			})
			.anySatisfy(element -> {
				assertThat(element.get("name").asText()).isEqualTo("stockPriceService");
				assertThat(element.get("description").asText()).containsIgnoringWhitespaces("""
					Lookup the stock price by company name""");
			});
	}

	@Test
	public void verify_McpAgent_remoteToolsDefinition() {
		Response response = mcpAgentRestClient()
			.accept(ContentType.JSON)
			.get("/remote-tool-definitions")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);

		JsonNode jsonNode = toJsonNode(response.body().asString());
		assertThat(jsonNode.size()).isEqualTo(2);
		JsonNode remoteServer1 = jsonNode.get("remoteServer1");
		JsonNode remoteServer2 = jsonNode.get("remoteServer2");
		assertThat(remoteServer1.isArray()).isTrue();
		List<JsonNode> remoteServer1Elements = StreamSupport.stream(remoteServer1.spliterator(), false).toList();
		assertThat(remoteServer1Elements)
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

		assertThat(remoteServer2.isArray()).isTrue();
		List<JsonNode> remoteServer2Elements = StreamSupport.stream(remoteServer2.spliterator(), false).toList();
		assertThat(remoteServer2Elements)
			.anySatisfy(element -> {
				assertThat(element.get("name").asText()).isEqualTo("weatherService");
				assertThat(element.get("description").asText()).containsIgnoringWhitespaces("""
					Get the weather for the given location or village or town or city.
					The getWeatherRequest takes location along with unit in Celsius or Fahrenheit.
					""");
			});
	}

	@Test
	public void verify_McpAgent_remoteServerStatuses() {
		Response response = mcpAgentRestClient()
			.accept(ContentType.JSON)
			.get("/remote-tool-status")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);

		JsonNode jsonNode = toJsonNode(response.body().asString());
		assertThat(jsonNode.size()).isEqualTo(2);
		JsonNode remoteServer1 = jsonNode.get("remoteServer1");
		JsonNode remoteServer2 = jsonNode.get("remoteServer2");
		assertThat(remoteServer1.asBoolean()).isTrue();
		assertThat(remoteServer2.asBoolean()).isTrue();
	}
}
