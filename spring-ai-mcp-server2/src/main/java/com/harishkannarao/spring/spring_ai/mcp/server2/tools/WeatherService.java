package com.harishkannarao.spring.spring_ai.mcp.server2.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class WeatherService implements AiTool {

	private final Logger log = LoggerFactory.getLogger(WeatherService.class);

	private final Map<String, BigDecimal> weather = Map.ofEntries(
		Map.entry("london", new BigDecimal("20")),
		Map.entry("chennai", new BigDecimal("37"))
	);

	@Tool(name = "weatherService",
		description = """
			Get the weather for the given location or village or town or city.
			The getWeatherRequest takes location along with unit in Celsius or Fahrenheit.
			""")
	public GetWeatherResponse apply(GetWeatherRequest getWeatherRequest) {
		log.info("getWeatherRequest {}", getWeatherRequest);
		BigDecimal value = weather.getOrDefault(getWeatherRequest.location().toLowerCase(), new BigDecimal("25"));
		GetWeatherResponse getWeatherResponse = new GetWeatherResponse(value, getWeatherRequest.unit());
		log.info("response {}", getWeatherResponse);
		return getWeatherResponse;
	}

	public record GetWeatherRequest(
		@ToolParam(description = "Town or City or Village or Location of the weather") String location,
		@ToolParam(description = "Unit in Celsius or Fahrenheit") Unit unit) {
	}

	public enum Unit {
		Celsius, Fahrenheit
	}

	public record GetWeatherResponse(
		BigDecimal weather,
		Unit unit) {
	}
}
