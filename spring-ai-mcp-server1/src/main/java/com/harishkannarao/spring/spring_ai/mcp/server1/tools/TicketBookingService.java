package com.harishkannarao.spring.spring_ai.mcp.server1.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;

@Component
public class TicketBookingService implements AiTool {

	private static final Logger log = LoggerFactory.getLogger(TicketBookingService.class);

	@Tool(name = "ticketBookingService",
		description = """
			Book tickets for a movie.
			Input parameters are movie name and total count of tickets to book
			""")
	public TicketBookingResponse apply(
		@ToolParam(description = "The name of a movie") String movieName,
		@ToolParam(description = "Count of tickets to book or number of tickets") Integer count) {
		log.info("bookingRequest {} {}", movieName, count);
		if (nonNull(movieName) && nonNull(count)) {
			List<String> seats = IntStream.range(0, count)
				.boxed()
				.map(index -> "A" + (index + 1))
				.toList();
			TicketBookingResponse bookingResponse = new TicketBookingResponse(true, seats);
			log.info("bookingResponse {}", bookingResponse);
			return bookingResponse;
		} else {
			TicketBookingResponse unsuccessfulResponse = new TicketBookingResponse(false, Collections.emptyList());
			log.info("bookingResponse {}", unsuccessfulResponse);
			return unsuccessfulResponse;
		}
	}

	public record TicketBookingRequest(
		@ToolParam(description = "The name of a movie") String movieName,
		@ToolParam(description = "Count of tickets to book or number of tickets") Integer count) {
	}

	public record TicketBookingResponse(
		boolean bookingSuccess,
		List<String> bookedSeats) {
	}
}


