package io.cote.chatdm.oracle.tool;


import io.cote.chatdm.journal.DMJournalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DMJournalTool {
    private static final Logger logger = LoggerFactory.getLogger(DMJournalTool.class);

    private final DMJournalRepository journalRepository;

    public DMJournalTool(DMJournalRepository journalRepository) {
        this.journalRepository = journalRepository;
        logger.info("DMJournalTool using {}", journalRepository.toString());
    }

    @Tool(description = "Used by the Dungeon Master (DM) to note down important things to remember. DM journal entries are usually for DM eyes only and are things like state of play, interesting developments to the plot, new NPCs, and other things a DM and story teller would keep in their notebook to remember and use ongoing.")
    public void chatDM_writeDMJournalEntry(@ToolParam(description="The DM journal entry to store.") String journalText,
                                    @ToolParam(description = Utils.CONTEXT_PARAM, required = false) String context) throws IOException {
        logger.debug("DM Journal entry to add:\n{}", journalText);
        journalRepository.addEntry(journalText);
    }

    @Tool(description = "Used to get entries from the Dungeon Master (DM) journal. These are notes the DM wanted to keep for later and may want to refer back to as they are crafting the adventure. Journal entries are not always for players eyes and are often for DM's eyes only.")
    public String chatDM_readDMJournal() throws IOException {
        String fullText = journalRepository.entries();
        if (fullText == null || fullText.isBlank()) {
            return "No entries found. Why not create the first?";
        }
        else {
            return journalRepository.entries();
        }
    }

}
