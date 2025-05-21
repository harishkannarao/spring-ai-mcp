package com.harishkannarao.spring.spring_ai.mcp.client.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class StockPriceService implements AiTool {

	private static final Logger log = LoggerFactory.getLogger(StockPriceService.class);

	@Tool(
		name = "stockPriceService",
		description = """
			Lookup the stock price by company name
			""")
	public Response lookupStockPrice(Request request) {
		log.info("request {}", request);
		long randomPositiveLong = new SecureRandom().longs(1, 201)
			.findAny()
			.orElse(1);
		Response response = new Response(randomPositiveLong);
		log.info("response {}", response);
		return response;
	}

	public record Request(@ToolParam(description = "Name of the company") String companyName) {
	}

	public record Response(Long price) {
	}
}


