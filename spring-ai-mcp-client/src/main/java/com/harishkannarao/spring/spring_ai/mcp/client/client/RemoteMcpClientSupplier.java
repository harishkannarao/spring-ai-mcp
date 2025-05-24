package com.harishkannarao.spring.spring_ai.mcp.client.client;

import io.modelcontextprotocol.client.McpSyncClient;

public interface RemoteMcpClientSupplier {

	String getServerName();
	McpSyncClient createClient();
}
