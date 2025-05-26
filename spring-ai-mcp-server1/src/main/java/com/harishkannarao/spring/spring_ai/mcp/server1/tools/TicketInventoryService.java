package com.harishkannarao.spring.spring_ai.mcp.server1.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Optional;

import static com.harishkannarao.spring.spring_ai.mcp.server1.util.Constants.X_REQUEST_ID;

@Component
public class TicketInventoryService implements AiTool {

	private static final Logger log = LoggerFactory.getLogger(TicketInventoryService.class);

	@Tool(name = "ticketInventoryService",
		description = "Get the available ticket count by movie name")
	public TicketLookupResponse apply(TicketLookupRequest lookupRequest, ToolContext toolContext) {
		try {
			Optional.ofNullable(toolContext.getContext().get(X_REQUEST_ID))
				.ifPresent(o -> MDC.put(X_REQUEST_ID, String.valueOf(o)));
			log.info("lookupRequest {}", lookupRequest);
			long randomPositiveLong = new SecureRandom().longs(0, 200)
				.findAny()
				.orElse(0);
			TicketLookupResponse lookupResponse = new TicketLookupResponse(randomPositiveLong);
			log.info("lookupResponse {}", lookupResponse);
			return lookupResponse;
		} finally {
			MDC.remove(X_REQUEST_ID);
		}
	}

	public record TicketLookupRequest(@ToolParam(description = "The name of a movie") String movieName) {
	}

	public record TicketLookupResponse(Long availability) {
	}
}


