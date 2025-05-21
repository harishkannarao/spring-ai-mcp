package com.harishkannarao.spring.spring_ai.mcp.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ChatClientConfiguration {

	@Bean
	public ChatClient chatClientWithTools(
		ChatModel chatModel,
		@Qualifier("remoteMcpTools") List<ToolCallback> remoteTools,
		@Qualifier("localMcpTools") List<ToolCallback> localTools) {
		List<ToolCallback> allTools = Stream.of(remoteTools, localTools)
			.flatMap(Collection::stream)
			.toList();
		return ChatClient.builder(chatModel)
			.defaultToolCallbacks(allTools)
			.defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}
}
