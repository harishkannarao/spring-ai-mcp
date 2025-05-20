package com.harishkannarao.spring.spring_ai.mcp.client.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class GoodBookSuggestionService
	implements Function<GoodBookSuggestionService.Request, GoodBookSuggestionService.Response> {

	private static final Logger log = LoggerFactory.getLogger(GoodBookSuggestionService.class);

	private final Map<String, List<String>> bookGenreMap = Map.ofEntries(
		Map.entry("fiction", List.of("book1", "book2", "book3", "book4")),
		Map.entry("mystery", List.of("book5", "book6", "book7", "book8"))
	);
	private final List<String> defaultBookList = List.of("book9", "book10", "book11");

	@Override
	public Response apply(Request request) {
		log.info("request {}", request);
		List<String> bookList = bookGenreMap
			.getOrDefault(request.genre().toLowerCase(), defaultBookList)
			.stream()
			.limit(request.limit())
			.toList();
		Response response = new Response(bookList);
		log.info("response {}", response);
		return response;
	}

	public record Request(
		@ToolParam(description = "Literary Genre or Book Genre") String genre,
		@ToolParam(description = """
			Total number of books to suggest.
			 If not provided explicitly then 1 is the default limit.
			""") Integer limit) {
	}

	public record Response(List<String> bookNames) {
	}
}


