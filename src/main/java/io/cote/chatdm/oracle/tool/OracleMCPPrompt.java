package io.cote.chatdm.oracle.tool;

import io.cote.chatdm.journal.DMJournalRepository;
import io.cote.chatdm.oracle.Oracle;
import io.cote.chatdm.oracle.repository.OracleRepository;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Experiments with the <a href="https://spec.modelcontextprotocol.io/specification/2024-11-05/server/prompts/">MCP
 * prompt Server Feature</a>.
 *
 * Some explanation of what a prompt does <a href="https://medium.com/@cstroliadavis/building-mcp-servers-13570f347c74">here</a>.
 * Basically, allows you to
 * build up a prompt to send to the client. So, you could
 * enrich the prompt with info, enforce your own prompt, put
 * in some multi-stepped process to execute.
 *
 * Claude's implementation is very crude. It pops open a dialog
 * box of the parameters for the user to fill out. It'd be cooler
 * if it did itself... maybe that's what role is for?
 */
//@Service
public class OracleMCPPrompt {

    private static final Logger logger = LoggerFactory.getLogger(OracleMCPPrompt.class);

    private final DMJournalRepository dmJournalRepository;

    public OracleMCPPrompt(DMJournalRepository dmJournalRepository) {
        this.dmJournalRepository = dmJournalRepository;
    }

    // Need to change to be startup prompt, taking in user name, preference for play,
    // etc. Set role to non-user, see what happens. Kind of clunky way to do things, though.
//    @Bean
//    public List<McpServerFeatures.SyncPromptRegistration> loadPrompts() {
//        logger.info("Registering MCP prompts.");
//        // create the prompt that will be used by MCP client
//        var prompt = new McpSchema.Prompt(
//                "ChatDM_DMJournal",
//                "Return the current DM journal.",
//                List.of(new McpSchema.PromptArgument(
//                        "context",
//                        "Any relevant context for why you're loading the journal", true)));
//
//        // link prompt to handler that is called in response to the prompt.
//        var promptRegistration = new McpServerFeatures.SyncPromptRegistration(prompt, getPromptRequest ->
//        {
//            String fullJournal = "";
//            try {
//                fullJournal = dmJournalRepository.entries();
//            } catch (IOException e) {
//                logger.error("Unable to load DM journal", e);
//                fullJournal = String.format("Errors retrieving journal {}", e.getMessage());
//            }
//
//            var journalResult = new McpSchema.PromptMessage(
//                    McpSchema.Role.USER, new
//                    McpSchema.TextContent(fullJournal));
//
//            // package the answer, a description and the actual message
//            return new McpSchema.GetPromptResult("Full DM Journal",
//                    List.of(journalResult));
//        });
//
//        logger.info("Registered MCP prompt: {}", promptRegistration);
//        return List.of(promptRegistration);
//    }

}
