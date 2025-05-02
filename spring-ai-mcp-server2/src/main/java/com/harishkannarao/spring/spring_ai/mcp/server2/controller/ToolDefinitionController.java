package com.harishkannarao.spring.spring_ai.mcp.server2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ToolDefinitionController {

	private final List<ToolCallback> toolCallbacks;
	private final ObjectMapper objectMapper;

	public ToolDefinitionController(
		List<ToolCallback> toolCallbacks,
		ObjectMapper objectMapper) {
		this.toolCallbacks = toolCallbacks;
		this.objectMapper = objectMapper;
	}

	@GetMapping("tool-definitions")
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
