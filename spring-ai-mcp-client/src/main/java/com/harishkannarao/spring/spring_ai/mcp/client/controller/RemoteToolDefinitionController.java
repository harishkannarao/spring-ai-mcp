package com.harishkannarao.spring.spring_ai.mcp.client.controller;

import com.harishkannarao.spring.spring_ai.mcp.client.client.RemoteMcpClientSupplier;
import com.harishkannarao.spring.spring_ai.mcp.client.tools.ToolsHelper;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RemoteToolDefinitionController {

	private static final Logger log = LoggerFactory.getLogger(RemoteToolDefinitionController.class);
	private final ToolsHelper toolsHelper;
	private final List<RemoteMcpClientSupplier> remoteMcpClientSuppliers;

	@Autowired
	public RemoteToolDefinitionController(
		ToolsHelper toolsHelper,
		List<RemoteMcpClientSupplier> remoteMcpClientSuppliers) {
		this.toolsHelper = toolsHelper;
		this.remoteMcpClientSuppliers = remoteMcpClientSuppliers;
	}

	@GetMapping("/remote-tool-definitions")
	public Map<String, List<McpSchema.Tool>> getToolDefinitions() {
		return toolsHelper.getRemoteToolsDefinition();
	}

	@GetMapping("/remote-tool-status")
	public Map<String, Boolean> getStatus() {
		Map<String, McpSyncClient> activeClients = toolsHelper.allActiveClients();
		return remoteMcpClientSuppliers.stream()
			.map(supplier -> Map.entry(supplier.getServerName(), activeClients.containsKey(supplier.getServerName())))
			.collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
