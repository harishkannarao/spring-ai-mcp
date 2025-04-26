package com.harishkannarao.spring.spring_ai.mcp.config;

import com.harishkannarao.spring.spring_ai.mcp.tools.WeatherService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolCallbackConfiguration {

	@Bean
	public ToolCallback createWeatherService(WeatherService weatherService) {
		return FunctionToolCallback
			.builder("weatherService", weatherService)
			.description("""
				Get the weather for the given location or village or town or city.
				The request takes location along with unit in Celsius or Fahrenheit.
				""")
			.inputType(WeatherService.Request.class)
			.build();
	}

}
