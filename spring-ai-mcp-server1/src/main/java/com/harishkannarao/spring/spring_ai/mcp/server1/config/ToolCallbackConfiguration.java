package com.harishkannarao.spring.spring_ai.mcp.server1.config;

import com.harishkannarao.spring.spring_ai.mcp.server1.tools.AiTool;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ToolCallbackConfiguration {

	@Bean
	public List<ToolCallback> toolCallbacks(List<AiTool> aiTools) {
		return Stream.of(ToolCallbacks.from(aiTools.toArray())).toList();
	}
}
