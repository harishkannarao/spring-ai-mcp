package com.harishkannarao.spring.spring_ai.mcp.client.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GoodBookSuggestionService implements AiTool {

	private static final Logger log = LoggerFactory.getLogger(GoodBookSuggestionService.class);

	private final Map<String, List<String>> bookGenreMap = Map.ofEntries(
		Map.entry("fiction", List.of("book1", "book2", "book3", "book4")),
		Map.entry("mystery", List.of("book5", "book6", "book7", "book8"))
	);
	private final List<String> defaultBookList = List.of("book9", "book10", "book11");

	@Tool(
		name = "goodBookSuggestionService",
		description = """
			Get suggestion of good books for a given genre.
				Also set the limit of books titles to return.
			""")
	public ListBooksResponse listBooks(ListBooksRequest listBooksRequest) {
		log.info("listBooksRequest {}", listBooksRequest);
		List<String> bookList = bookGenreMap
			.getOrDefault(listBooksRequest.genre().toLowerCase(), defaultBookList)
			.stream()
			.limit(listBooksRequest.limit())
			.toList();
		ListBooksResponse listBooksResponse = new ListBooksResponse(bookList);
		log.info("response {}", listBooksResponse);
		return listBooksResponse;
	}

	public record ListBooksRequest(
		@ToolParam(description = "Literary Genre or Book Genre") String genre,
		@ToolParam(description = """
			Total number of books to suggest.
			 If not provided explicitly then 1 is the default limit.
			""") Integer limit) {
	}

	public record ListBooksResponse(List<String> bookNames) {
	}
}


