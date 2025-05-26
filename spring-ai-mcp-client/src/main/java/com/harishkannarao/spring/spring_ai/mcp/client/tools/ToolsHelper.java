package com.harishkannarao.spring.spring_ai.mcp.client.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.spring.spring_ai.mcp.client.client.RemoteMcpClientSupplier;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ToolsHelper implements DisposableBean {
	private final ScheduledExecutorService executorService =
		Executors.newSingleThreadScheduledExecutor();
	private final Logger logger = LoggerFactory.getLogger(ToolsHelper.class);
	private final ConcurrentHashMap<String, McpSyncClient> mcpClients = new ConcurrentHashMap<>();
	private final AtomicReference<List<ToolCallback>> activeToolCallbacks = new AtomicReference<>(Collections.emptyList());
	private final ObjectMapper objectMapper;
	private final List<AiTool> localAiTools;
	private final List<RemoteMcpClientSupplier> remoteMcpClientSuppliers;

	@Autowired
	public ToolsHelper(
		ObjectMapper objectMapper,
		List<AiTool> localAiTools,
		List<RemoteMcpClientSupplier> remoteMcpClientSuppliers) {
		this.objectMapper = objectMapper;
		this.localAiTools = localAiTools;
		this.remoteMcpClientSuppliers = remoteMcpClientSuppliers;
		CompletableFuture.runAsync(this::reInitializeClient, executorService);
		executorService.scheduleWithFixedDelay(this::reInitializeClient, 10, 10, TimeUnit.SECONDS);
	}

	public record ClientActiveResult(Boolean active, Map.Entry<String, McpSyncClient> entry) {
	}

	public Map<String, McpSyncClient> allActiveClients() {
		return mcpClients.entrySet().stream()
			.map(entry ->
				CompletableFuture.supplyAsync(() -> new ClientActiveResult(isMcpClientActive(entry.getKey(), entry.getValue()), entry)))
			.toList()
			.stream()
			.map(CompletableFuture::join)
			.filter(ClientActiveResult::active)
			.map(ClientActiveResult::entry)
			.collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public List<ToolCallback> getActiveTools() {
		return activeToolCallbacks.get();
	}

	public List<McpSchema.Tool> getLocalToolsDefinition() {
		return Stream.of(ToolCallbacks.from(localAiTools.toArray()))
			.map(toolCallback -> {
				try {
					return new McpSchema.Tool(
						toolCallback.getToolDefinition().name(),
						toolCallback.getToolDefinition().description(),
						objectMapper.readValue(
							toolCallback.getToolDefinition().inputSchema(), McpSchema.JsonSchema.class)
					);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.toList();
	}

	public Map<String, List<McpSchema.Tool>> getRemoteToolsDefinition() {
		return allActiveClients().entrySet().stream()
			.map(entry ->
				Map.entry(entry.getKey(), entry.getValue().listTools().tools()))
			.collect(Collectors.toUnmodifiableMap(
				Map.Entry::getKey,
				Map.Entry::getValue,
				(o, o2) -> o2));
	}

	private void reInitializeClient() {
		logger.info("Re-Initializing remote mcp clients");
		remoteMcpClientSuppliers.stream()
			.map(supplier -> CompletableFuture.runAsync(() -> {
				try {
					if (!mcpClients.containsKey(supplier.getServerName())) {
						mcpClients.putIfAbsent(supplier.getServerName(), supplier.createClient());
					}
					McpSyncClient existingClient = mcpClients.get(supplier.getServerName());
					if (!isMcpClientActive(supplier.getServerName(), existingClient)) {
						mcpClients.put(supplier.getServerName(), supplier.createClient());
						try {
							existingClient.closeGracefully();
						} catch (Exception e) {
							logger.warn("Remote Server {} failed to close existing client. Exception {}", supplier.getServerName(), e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					logger.error("Remote Server {} cannot be initialised. Exception {}", supplier.getServerName(), e.getMessage(), e);
				}
			}))
			.toList()
			.forEach(CompletableFuture::join);
		activeToolCallbacks.set(checkAndGetActiveTools());
	}

	private List<ToolCallback> checkAndGetActiveTools() {
		List<McpSyncClient> activeRemoteClients = allActiveClients().values().stream().toList();
		Stream<ToolCallback> remoteToolCallbacks = SyncMcpToolCallbackProvider
			.syncToolCallbacks(activeRemoteClients).stream();
		Stream<ToolCallback> localToolCallbacks = Stream.of(ToolCallbacks.from(localAiTools.toArray()));
		return Stream.concat(localToolCallbacks, remoteToolCallbacks).toList();
	}

	private boolean isMcpClientActive(String serverName, McpSyncClient mcpSyncClient) {
		try {
			mcpSyncClient.ping();
			return true;
		} catch (Exception e) {
			logger.warn("Remote Server {} is not available. Exception {}", serverName, e.getMessage(), e);
		}
		return false;
	}

	@Override
	public void destroy() throws Exception {
		executorService.shutdown();
	}
}
