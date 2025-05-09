package com.harishkannarao.spring.spring_ai.mcp.client.controller;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RemoteToolDefinitionController {

	private final Map<String, McpSyncClient> mcpSyncClientMap;

	@Autowired
	public RemoteToolDefinitionController(
		Map<String, McpSyncClient> mcpSyncClientMap) {
		this.mcpSyncClientMap = mcpSyncClientMap;
	}

	@GetMapping("/remote-tool-definitions")
	public Map<String, List<McpSchema.Tool>> getToolDefinitions() {
		return mcpSyncClientMap.entrySet().stream()
			.map(entry ->
				Map.entry(entry.getKey(), entry.getValue().listTools().tools()))
			.collect(Collectors.toUnmodifiableMap(
				Map.Entry::getKey,
				Map.Entry::getValue,
				(o, o2) -> o2));
	}
}
