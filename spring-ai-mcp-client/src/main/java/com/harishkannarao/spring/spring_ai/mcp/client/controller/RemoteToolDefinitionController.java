package com.harishkannarao.spring.spring_ai.mcp.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RemoteToolDefinitionController {

	private final List<ToolCallback> toolCallbacks;
	private final ObjectMapper objectMapper;

	@Autowired
	public RemoteToolDefinitionController(
		@Qualifier("remoteMcpTools") List<ToolCallback> toolCallbacks,
		ObjectMapper objectMapper) {
		this.toolCallbacks = toolCallbacks;
		this.objectMapper = objectMapper;
	}

	@GetMapping("remote-tool-definitions")
	public List<ToolDefinitionDto> getToolDefinitions() {
		return toolCallbacks.stream()
			.map(toolCallback -> {
				try {
					return new ToolDefinitionDto(
						toolCallback.getToolDefinition().name(),
						toolCallback.getToolDefinition().description(),
						objectMapper.readTree(toolCallback.getToolDefinition().inputSchema())
					);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.toList();
	}

	public record ToolDefinitionDto(
		String name,
		String description,
		JsonNode inputSchema
	) {

	}
}
