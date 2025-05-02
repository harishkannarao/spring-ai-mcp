package com.harishkannarao.spring.spring_ai.mcp.client.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpClientConfiguration {

	@Bean("remoteMcpServer1")
	public McpSyncClient createClientForServer1(
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server1.url}")
		String url,
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server1.sse-endpoint}")
		String sseEndpoint
	) {
		McpSyncClient mcpSyncClient = McpClient
			.sync(HttpClientSseClientTransport.builder(url).sseEndpoint(sseEndpoint).build())
			.build();
		mcpSyncClient.initialize();
		return mcpSyncClient;
	}

	@Bean("remoteMcpServer2")
	public McpSyncClient createClientForServer2(
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server2.url}")
		String url,
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server2.sse-endpoint}")
		String sseEndpoint
	) {
		McpSyncClient mcpSyncClient = McpClient
			.sync(HttpClientSseClientTransport.builder(url).sseEndpoint(sseEndpoint).build())
			.build();
		mcpSyncClient.initialize();
		return mcpSyncClient;
	}

	@Bean
	@Qualifier("remoteMcpTools")
	public List<ToolCallback> remoteMcpTools(List<McpSyncClient> mcpSyncClients) {
		return SyncMcpToolCallbackProvider.syncToolCallbacks(mcpSyncClients);
	}
}
