package io.cote.chatdm.dnd;


import io.cote.chatdm.dmstore.FileDMStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


/**
 * AI tools to assist in the playing D&D: a bootstrapping tool to start with a prompt and instruct the AI to use the
 * other tools available.
 */
@Service
public class PlayDnDTool {

    private static final Logger logger = LoggerFactory.getLogger(PlayDnDTool.class);

    private final FileDMStoreRepository dmStoreRepository;

    public PlayDnDTool(FileDMStoreRepository dmStoreRepository) {
        this.dmStoreRepository = dmStoreRepository;
    }

    @Tool(name = "Play_DnD", description = "When you start playing D&D, consult and follow these instructions before doing anything else. If there are no instructions, ask for instructions on how to play Dungeons and Dragons, offering three different styles.")
    public String playDnD() throws IOException {
        return Optional.of(dmStoreRepository.loadToString("prompts/dnd-init-prompt.txt")).orElse("");
    }

}
