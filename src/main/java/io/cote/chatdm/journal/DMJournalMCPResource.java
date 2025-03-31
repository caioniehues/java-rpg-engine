package io.cote.chatdm.journal;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static io.modelcontextprotocol.spec.McpSchema.Role.ASSISTANT;

@Service
public class DMJournalMCPResource {

    private static final Logger logger = LoggerFactory.getLogger(DMJournalMCPResource.class);

    private final DMJournalRepository journalRepository;

    public DMJournalMCPResource(DMJournalRepository journalRepository) {
        this.journalRepository = journalRepository;
        logger.info("DMJournalMCPResource using {}", journalRepository.toString());
    }

    @Bean
    private List<McpServerFeatures.SyncResourceSpecification> loadJournalResource() {
        var journalInfoResource = new McpSchema.Resource(
                "file:///chatdm/journal/dm-journal.md",
                "ChatDM_DMJournal",
                "Retrieves the full Dungeon Master (DM) Journal. Entries are returned oldest to latest, seperated by a date header",
                "text/markdown",
                new McpSchema.Annotations(List.of(ASSISTANT), 0.0)
        );

        var resourceSpecification = new McpServerFeatures.SyncResourceSpecification(
                journalInfoResource,
                (exchange, request) -> {
                    try {
                        exchange.getClientInfo();
                        String journal = journalRepository.entries();

                        return new McpSchema.ReadResourceResult(
                                List.of(new McpSchema.TextResourceContents(
                                        request.uri(),
                                        "text/markdown",
                                        journal)));
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to retrieve DM Journal", e);
                    }
                });

        logger.info("Loaded DMJournalMCPResource: {}", resourceSpecification);
        return List.of(resourceSpecification);
    }


}
