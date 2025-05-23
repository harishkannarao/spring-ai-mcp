package com.harishkannarao.spring.spring_ai.mcp.client.config;

import org.springframework.ai.bedrock.cohere.BedrockCohereEmbeddingModel;
import org.springframework.ai.bedrock.converse.BedrockProxyChatModel;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AwsBedrockConfiguration {

	@Bean
	@ConditionalOnProperty(name = "app.ai.chat.provider", havingValue = "aws")
	@Primary
	public ChatModel defaultChatModel(BedrockProxyChatModel bedrockProxyChatModel) {
		return bedrockProxyChatModel;
	}
}
