package com.pravin.railway.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;

@Service
public class RailwayAiClient {

    private final ChatClient chatClient;
    private final ToolCallbackProvider mcpTools;
    private final McpSyncClient mcpSyncClient;

    public RailwayAiClient(
            ChatClient.Builder chatClientBuilder,
            ToolCallbackProvider mcpTools,
            McpSyncClient mcpSyncClient) {

        this.chatClient = chatClientBuilder.build();
        this.mcpTools = mcpTools;
        this.mcpSyncClient = mcpSyncClient;
    }

    public String askRailwayQuestion(String question) {
        String policy = getNotificationPolicy();
        return chatClient.prompt()
                .system("""
                    You are an enterprise railway operations assistant.

                    Responsibilities:
                    1. Answer train status questions using getTrainStatus.
                    2. Answer incident questions using listOpenIncidents.
                    3. Answer railway policy questions using searchNotificationPolicy.

                    Rules:
                    - Use the relevant tool when operational information is requested.
                    - Never invent train locations, train status, or incidents.
                    - Never assume a train is delayed or on time without tool data.
                    - For policy questions, use retrieved policy content.
                    - Clearly identify unavailable or missing information.
                    - Distinguish retrieved facts from recommendations.
                    - Do not execute operational changes unless an explicitly
                      authorized tool supports them.
                    """)
                .user("""   
                            Railway Notification Policy:
                            %s
                            Question:
                            %s
                      """.formatted(policy, question))
                .tools(mcpTools)
                .call()
                .content();
    }

    public String getNotificationPolicy() {

        McpSchema.ReadResourceResult result =
                mcpSyncClient.readResource(new McpSchema.ReadResourceRequest("railway://policies/notification"));

        return result.contents().stream()
                .filter(content -> content instanceof McpSchema.TextResourceContents)
                .map(content ->
                        ((McpSchema.TextResourceContents) content).text())
                .collect(java.util.stream.Collectors.joining("\n\n"));
    }
}