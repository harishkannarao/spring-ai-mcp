package com.harishkannarao.spring.spring_ai.mcp.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocalToolDefinitionController {

	private final ObjectMapper objectMapper;
	private final List<ToolCallback> toolCallbacks;

	@Autowired
	public LocalToolDefinitionController(
		ObjectMapper objectMapper,
		@Qualifier("localMcpTools") List<ToolCallback> toolCallbacks) {
		this.objectMapper = objectMapper;
		this.toolCallbacks = toolCallbacks;
	}

	@GetMapping("/tool-definitions")
	public List<McpSchema.Tool> getToolDefinitions() {
		return toolCallbacks.stream()
			.map(toolCallback -> {
				try {
					return new McpSchema.Tool(
						toolCallback.getToolDefinition().name(),
						toolCallback.getToolDefinition().description(),
						objectMapper.readValue(
							toolCallback.getToolDefinition().inputSchema(), McpSchema.JsonSchema.class)
					);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.toList();
	}
}
