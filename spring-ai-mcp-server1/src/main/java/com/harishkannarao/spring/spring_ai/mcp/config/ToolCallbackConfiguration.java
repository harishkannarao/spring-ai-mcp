package com.harishkannarao.spring.spring_ai.mcp.config;

import com.harishkannarao.spring.spring_ai.mcp.tools.TicketBookingService;
import com.harishkannarao.spring.spring_ai.mcp.tools.TicketInventoryService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolCallbackConfiguration {

	@Bean
	public ToolCallback createTicketInventoryService(TicketInventoryService ticketInventoryService) {
		return FunctionToolCallback
			.builder("ticketInventoryService", ticketInventoryService)
			.description("Get the available ticket count by movie name")
			.inputType(TicketInventoryService.Request.class)
			.build();
	}

	@Bean
	public ToolCallback createTicketBookingService(TicketBookingService ticketBookingService) {
		return FunctionToolCallback
			.builder("ticketBookingService", ticketBookingService)
			.description("""
				Book tickets for a movie.
				Input parameters are movie name and total number of tickets to book
				""")
			.inputType(TicketBookingService.Request.class)
			.build();
	}
}
