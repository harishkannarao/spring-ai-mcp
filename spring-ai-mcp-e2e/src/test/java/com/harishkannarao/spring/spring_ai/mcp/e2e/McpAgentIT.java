package com.harishkannarao.spring.spring_ai.mcp.e2e;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class McpAgentIT extends AbstractBaseIT {

	@Test
	public void call_multiple_tools_from_local_tool_and_multiple_mcp_servers() {
		String input = """
			How many tickets are available for Avatar Movie? Book me 3 tickets if available.
			
			Also tell me what is the weather like in London?
			
			Suggest some 5 good book in fiction genre to read while travelling.
			""";

		Response response = mcpAgentRestClient()
			.accept(ContentType.TEXT)
			.param("q", input)
			.get("/chat-with-tools");

		assertThat(response.statusCode()).isEqualTo(200);

		String respText = response.body().asString();
		assertThat(respText)
			.containsIgnoringCase("Avatar")
			.containsIgnoringCase("available")
			.containsIgnoringCase("success")
			.containsIgnoringCase("A1")
			.containsIgnoringCase("A2")
			.containsIgnoringCase("A3")
			.containsIgnoringCase("London")
			.containsIgnoringCase("20")
			.satisfiesAnyOf(
				text -> assertThat(text).containsIgnoringCase("Book 1"),
				text -> assertThat(text).containsIgnoringCase("Book1")
			)
			.satisfiesAnyOf(
				text -> assertThat(text).containsIgnoringCase("Book 2"),
				text -> assertThat(text).containsIgnoringCase("Book2")
			);
	}
}
