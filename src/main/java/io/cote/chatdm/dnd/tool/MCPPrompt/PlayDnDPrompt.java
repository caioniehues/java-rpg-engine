package io.cote.chatdm.dnd.tool.MCPPrompt;

import io.cote.chatdm.dmstore.FileDMStoreRepository;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.util.*;

/**
 * MCP Prompts used to start the game.
 * <p>
 * You can use StringTemplates for the prompt text, passing in the arguments from the MCP Client. The argument names
 * cannot have spaces, unfortunitly.
 */
@Component
public class PlayDnDPrompt {

    private static final Logger logger = LoggerFactory.getLogger(PlayDnDPrompt.class);

    private final FileDMStoreRepository dmStoreRepository;

    public PlayDnDPrompt(FileDMStoreRepository dmStoreRepository) {
        this.dmStoreRepository = dmStoreRepository;
    }

    @Bean
    public List<McpServerFeatures.SyncPromptRegistration> loadPrompts(ParameterValueMapper parameterValueMapper) {

        // This is pretty crazy stuff down here...
        // But at least I have a clean, generalized way to build
        // prompts.

        // Boot Strap the ChatDM
        PromptBuilder builder = new PromptBuilder();
        builder.setPromptName("ChatDM_PlayDnD_Customized");
        builder.setPromptDescription("Starts playing D&D with input from the player.");
        builder.setPromptTemplateName("prompts/dnd-init-prompt.st");
        builder.addArg("ExtraPlayerInstructions", "Ideas and direction from the player.");

        logger.info("Registered MCP prompt: {}", builder.promptName);
        return List.of(builder.build());
    }

    /**
     * Builder to take care of the PromptRegistration building.
     */
    class PromptBuilder {
        private final Map<String, String> args = new TreeMap<>();
        private String promptName;
        private String promptDescription;
        private String promptTemplateName;

        public void setPromptName(String promptName) {
            this.promptName = promptName;
        }

        public void setPromptDescription(String promptDescription) {
            this.promptDescription = promptDescription;
        }

        public void setPromptTemplateName(String promptTemplateName) {
            this.promptTemplateName = promptTemplateName;
        }

        void addArg(String name, String argDescription) {
            args.put(name, argDescription);
        }

        void addArgs(Map<String, String> args) {
            args.putAll(args);
        }

        McpServerFeatures.SyncPromptRegistration build() {


            logger.info("Registering MCP prompts.");
            // Create the prompt that will be used by MCP client
            // In Claude, we will collect "Extra Player Instructions"
            // Where the player can add in addition info to start with.
            // It uses String Template, so the prompt template text
            // <ExtraPlayerInstructions>
            // will be replaced with the extra information.
            // StringTemplate format here: https://github.com/antlr/stringtemplate4/blob/master/doc/introduction.md

            // copy over any args.
            List<McpSchema.PromptArgument> mcpArgs = new java.util.ArrayList<>();
            for (Map.Entry<String, String> entry : args.entrySet()) {
                // TK I hard coded required to false to be ultra flexible.
                mcpArgs.add(new McpSchema.PromptArgument(entry.getKey(), entry.getValue(), false));
            }

            McpSchema.Prompt mcpPrompt = new McpSchema.Prompt(
                    promptName,
                    promptDescription,
                    mcpArgs
            );

            // link prompt to handler that is called in response to the prompt.
            var promptRegistration = new McpServerFeatures.SyncPromptRegistration(mcpPrompt, promptRequest ->
            {
                ST promptTemplate = new ST("");
                try {
                    String prompt = Optional.of(dmStoreRepository.
                                    loadToString(promptTemplateName)).
                            orElse("");
                    promptTemplate = new ST(prompt);
                } catch (IOException e) {
                    logger.error("Unable to load DM journal", e);
                    promptTemplate = new ST("Error loading prompt: <error>");
                    promptTemplate.add("error", e.getMessage());
                }

                // Copy arguments passed in by the user to fill out
                // String Template for prompt.
                Map<String, Object> args = promptRequest.arguments();
                for (Map.Entry<String, Object> entry : args.entrySet()) {
                    promptTemplate.add(entry.getKey(), entry.getValue());
                }

                var promptResult = new McpSchema.PromptMessage(
                        McpSchema.Role.USER, new
                        McpSchema.TextContent(promptTemplate.render())
                );

                // package the answer, a description and the actual message
                return new McpSchema.GetPromptResult(
                        "Instructions for playing D&D",
                        List.of(promptResult));
            });

            return promptRegistration;
        }
    }
}
