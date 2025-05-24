package com.harishkannarao.spring.spring_ai.mcp.client.controller;

import com.harishkannarao.spring.spring_ai.mcp.client.tools.ToolsHelper;
import com.harishkannarao.spring.spring_ai.mcp.client.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/question-template.st");
	private final ChatClient chatClient;
	private final ToolsHelper toolsHelper;

	public ChatController(
		ChatClient chatClient,
		ToolsHelper toolsHelper) {
		this.chatClient = chatClient;
		this.toolsHelper = toolsHelper;
	}

	@GetMapping("/chat-with-tools")
	public String chatWithTools(@RequestParam String q) {
		log.info("Question {}", q);
		PromptTemplate promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("question", q);
		Message userMessage = promptTemplate.createMessage();
		Message systemMessage = new SystemMessage(
			"You are a helpful AI Assistant answering questions");
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		return chatClient
			.prompt(prompt)
			.toolCallbacks(toolsHelper.allActiveTools())
			.toolContext(Map.of(Constants.X_REQUEST_ID, MDC.get(Constants.X_REQUEST_ID)))
			.call()
			.content();
	}
}
