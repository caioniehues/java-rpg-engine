package io.cote.chatdm;

import io.cote.chatdm.dc.DifficultyClassTool;
import io.cote.chatdm.dice.DiceRollerTool;
import io.cote.chatdm.dnd.PlayDnDTool;
import io.cote.chatdm.journal.DMJournalTool;
import io.cote.chatdm.oracle.OracleTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ChatDmApplication {

    private static final Logger logger = LoggerFactory.getLogger(ChatDmApplication.class);

    public static void main(String[] args) {
        logger.info("Starting ChatDM.");
        SpringApplication.run(ChatDmApplication.class, args);

    }

    //
    // MCP tool loading.
    // TK seems like this could be done in a properties file
    // where I list each tool I want to include.
    //

    @Bean
    public ToolCallbackProvider registerOraclesTool(OracleTool tool) {
        return MethodToolCallbackProvider.builder().toolObjects(tool).build();
    }

    @Bean
    public ToolCallbackProvider registerPlayDndTool(PlayDnDTool tool) {
        return MethodToolCallbackProvider.builder().toolObjects(tool).build();
    }

    @Bean
    public ToolCallbackProvider registerDMJournalTool(DMJournalTool tool) {
        return MethodToolCallbackProvider.builder().toolObjects(tool).build();
    }

    @Bean
    public ToolCallbackProvider registerDiceRollerTool(DiceRollerTool tool) {
        return MethodToolCallbackProvider.builder().toolObjects(tool).build();
    }

    @Bean
    public ToolCallbackProvider registerDCTool(DifficultyClassTool tool) {
        return MethodToolCallbackProvider.builder().toolObjects(tool).build();
    }


}
