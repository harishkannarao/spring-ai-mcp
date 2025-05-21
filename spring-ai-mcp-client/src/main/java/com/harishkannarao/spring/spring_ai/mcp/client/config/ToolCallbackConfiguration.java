package com.harishkannarao.spring.spring_ai.mcp.client.config;

import com.harishkannarao.spring.spring_ai.mcp.client.tools.AiTool;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ToolCallbackConfiguration {

	@Bean
	@Qualifier("localMcpTools")
	@Primary
	public List<ToolCallback> localMcpTools(List<AiTool> localTools) {
		return Stream.of(ToolCallbacks.from(localTools.toArray())).toList();
	}
}
