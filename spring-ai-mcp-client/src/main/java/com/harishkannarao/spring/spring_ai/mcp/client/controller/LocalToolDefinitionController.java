package com.harishkannarao.spring.spring_ai.mcp.client.controller;

import com.harishkannarao.spring.spring_ai.mcp.client.tools.ToolsHelper;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocalToolDefinitionController {

	private final ToolsHelper toolsHelper;

	@Autowired
	public LocalToolDefinitionController(ToolsHelper toolsHelper) {
		this.toolsHelper = toolsHelper;
	}

	@GetMapping("/tool-definitions")
	public List<McpSchema.Tool> getToolDefinitions() {
		return toolsHelper.getLocalToolsDefinition();
	}
}
