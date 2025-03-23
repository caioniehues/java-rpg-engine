package io.cote.chatdm.journal.MCPResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cote.chatdm.journal.DMJournalRepository;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class DMJournalResource {

    private static final Logger logger = LoggerFactory.getLogger(DMJournalResource.class);

    private final DMJournalRepository journalRepository;

    public DMJournalResource(DMJournalRepository journalRepository) {
        this.journalRepository = journalRepository;
        logger.info("Created DMJournalResource using {}", journalRepository.toString());
    }

    private String getJournal() {
        try {
            return journalRepository.entries();
        } catch (IOException e) {
            return "Error retrieving journal: " + e.getMessage();
        }
    }

    @Bean
    public List<McpServerFeatures.SyncResourceRegistration> registerResources
            () {
        /* An MCPSchema.Resource has:
        @JsonProperty("uri") String uri,
		@JsonProperty("name") String name,
		@JsonProperty("description") String description,
		@JsonProperty("mimeType") String mimeType,
		@JsonProperty("annotations") Annotations annotations) implements Annotated {

         */

        // This uri could use some work. Could generalize this into
        // something that just retrieves a file from the .chatdm.
        // or keep it abstract like this for when we want to switch
        // out the implementation of the .chatdm directory.
        McpSchema.Resource dmJournalResource = new McpSchema.Resource(
                "chatdm://dmjournal/read",
                "ChatDM_DMJournal",
                "The current DM Journal.",
                "text/markdown",
                new McpSchema.Annotations(
                        List.of(McpSchema.Role.ASSISTANT),
                        Double.valueOf(1.0))
        );

        // TK should validate this with CommonMark or something.
        // https://github.com/commonmark/commonmark-java
        String journalText = getJournal();

        // LEFT OFF: building this up based on what I see here.
        // I think I need to change the jsonContent to have the actual
        // reply, but change it to mimetype text/markdown and just return
        // the raw markdown text?
        McpServerFeatures.SyncResourceRegistration resourceRegistration =
                new McpServerFeatures.SyncResourceRegistration(
                        dmJournalResource,
                        request -> {
                            try {
                                String jsonContent = new ObjectMapper().writeValueAsString(getJournal());
                                return new McpSchema.ReadResourceResult(
                                        List.of(new McpSchema.TextResourceContents(request.uri(), "application/markdown", journalText)));
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to generate system info", e);
                            }
                        });

        return List.of(resourceRegistration);
    }
}
