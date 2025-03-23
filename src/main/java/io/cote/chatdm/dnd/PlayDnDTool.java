package io.cote.chatdm.dnd;


import io.cote.chatdm.dmstore.FileDMStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

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

    @Tool(name = "ChatDM_Play_DnD", description = "When you start playing D&D, consult and follow these instructions before doing anything else. If there are no instructions, ask for instructions on how to play Dungeons and Dragons, offering three different styles.")
    public String chatDM_playDnD(
            @ToolParam(description = "Extra information to take into account when starting to play such as user ideas or instructions, ongoing style and story, etc. This information will be included in the returned information.", required = false) String extraInfo) throws IOException {
        ST promptTemplate = new ST(
                Optional.of(dmStoreRepository.loadToString("prompts/dnd-init-prompt.st")).orElse("")
        );
        promptTemplate.add("ExtraInfo", extraInfo);
        return promptTemplate.render();
    }


}
