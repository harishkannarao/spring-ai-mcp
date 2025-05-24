package com.harishkannarao.spring.spring_ai.mcp.client.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class RemoteServer1Supplier implements RemoteMcpClientSupplier {
	private final String url;
	private final String sseEndpoint;
	private final String authToken;

	public RemoteServer1Supplier(
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server1.url}")
		String url,
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server1.sse-endpoint}")
		String sseEndpoint,
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server1.auth-token}")
		String authToken) {
		this.url = url;
		this.sseEndpoint = sseEndpoint;
		this.authToken = authToken;
	}


	@Override
	public String getServerName() {
		return "remoteServer1";
	}

	@Override
	public McpSyncClient createClient() {
		// This consumer can be a standalone spring component to dynamically fetch the auth token
		// from an Identity Provider
		Consumer<HttpRequest.Builder> perRequestHeaderSetter = (HttpRequest.Builder builder)
			-> {
			builder.setHeader("x_request_id", "mcp-cli-" + UUID.randomUUID());
			builder.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
		};

		McpSyncClient mcpSyncClient = McpClient
			.sync(HttpClientSseClientTransport
				.builder(url)
				.sseEndpoint(sseEndpoint)
				.customizeRequest(perRequestHeaderSetter)
				.build())
			.build();
		mcpSyncClient.initialize();
		return mcpSyncClient;
	}
}
