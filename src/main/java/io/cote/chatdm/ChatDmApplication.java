package io.cote.chatdm;

import io.cote.chatdm.dice.DiceRollerTool;
import io.cote.chatdm.dnd.PlayDnDTool;
import io.cote.chatdm.journal.DMJournalTool;
import io.cote.chatdm.oracle.OracleTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
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
    // TK seems like this could be done in a properties file?
    //

    @Bean
    public List<ToolCallback> registerOracles(OracleTool oracleTool) {
        return List.of(ToolCallbacks.from(oracleTool));
    }

    @Bean
    public List<ToolCallback> registerPlay(PlayDnDTool tool) {
        return List.of(ToolCallbacks.from(tool));
    }

    @Bean
    public List<ToolCallback> registerDMJournal(DMJournalTool tool) {
        return List.of(ToolCallbacks.from(tool));
    }

    @Bean
    public List<ToolCallback> registerDiceRoller(DiceRollerTool tool) {
        return List.of(ToolCallbacks.from(tool));
    }
}
