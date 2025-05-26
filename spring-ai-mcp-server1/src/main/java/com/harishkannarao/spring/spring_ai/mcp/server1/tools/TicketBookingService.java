package com.harishkannarao.spring.spring_ai.mcp.server1.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class TicketBookingService implements AiTool {

	private static final Logger log = LoggerFactory.getLogger(TicketBookingService.class);

	@Tool(name = "ticketBookingService",
		description = """
			Book tickets for a movie.
			Input parameters are movie name and total number of tickets to book
			""")
	public TicketBookingResponse apply(TicketBookingRequest bookingRequest) {
		log.info("bookingRequest {}", bookingRequest);
		List<String> seats = IntStream.range(0, bookingRequest.count())
			.boxed()
			.map(index -> "A" + (index + 1))
			.toList();
		TicketBookingResponse bookingResponse = new TicketBookingResponse(true, seats);
		log.info("bookingResponse {}", bookingResponse);
		return bookingResponse;
	}

	public record TicketBookingRequest(
		@ToolParam(description = "The name of a movie") String movieName,
		@ToolParam(description = "Number of tickets to book") Integer count) {
	}

	public record TicketBookingResponse(
		boolean bookingSuccess,
		List<String> bookedSeats) {
	}
}


