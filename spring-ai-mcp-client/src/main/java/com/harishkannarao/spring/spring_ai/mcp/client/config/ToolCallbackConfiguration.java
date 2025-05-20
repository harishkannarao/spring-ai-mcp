package com.harishkannarao.spring.spring_ai.mcp.client.config;

import com.harishkannarao.spring.spring_ai.mcp.client.tools.GoodBookSuggestionService;
import com.harishkannarao.spring.spring_ai.mcp.client.tools.StockPriceService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ToolCallbackConfiguration {

	@Bean
	@Qualifier("localMcpTools")
	public List<ToolCallback> localMcpTools(List<ToolCallback> localTools) {
		return localTools;
	}

	@Bean
	public ToolCallback createGoodBookSuggestionService(
		GoodBookSuggestionService function) {
		return FunctionToolCallback
			.builder("goodBookSuggestionService", function)
			.description("""
				Get suggestion of good books for a given genre.
					Also set the limit of books titles to return.
				""")
			.inputType(GoodBookSuggestionService.Request.class)
			.build();
	}

	@Bean
	public ToolCallback createStockPriceService(StockPriceService function) {
		return FunctionToolCallback
			.builder("stockPriceService", function)
			.description("""
				Lookup the stock price by company name
				""")
			.inputType(StockPriceService.Request.class)
			.build();
	}
}
