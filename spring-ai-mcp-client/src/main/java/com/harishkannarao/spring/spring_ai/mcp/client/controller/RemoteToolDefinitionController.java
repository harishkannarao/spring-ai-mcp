package com.harishkannarao.spring.spring_ai.mcp.client.controller;

import com.harishkannarao.spring.spring_ai.mcp.client.tools.ToolsHelper;
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

	@Autowired
	public RemoteToolDefinitionController(ToolsHelper toolsHelper) {
		this.toolsHelper = toolsHelper;
	}

	@GetMapping("/remote-tool-definitions")
	public Map<String, List<McpSchema.Tool>> getToolDefinitions() {
		return toolsHelper.getRemoteToolsDefinition();
	}

	@GetMapping("/remote-tool-status")
	public Map<String, Object> getStatus() {
		return toolsHelper.allActiveClients().entrySet().stream()
			.map(entry -> Map.entry(entry.getKey(), entry.getValue().ping()))
			.collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
