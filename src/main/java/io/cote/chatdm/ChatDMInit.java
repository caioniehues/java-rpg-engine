package io.cote.chatdm;

import io.cote.chatdm.config.ChatDMProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Setups up initial home directory or other stores for the ChatDM.
 */
@Component
public class ChatDMInit implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ChatDMInit.class);
    private final ChatDMProperties properties;

    public ChatDMInit(ChatDMProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path dir = properties.getChatDMDirPath();
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
            logger.info("Created .chatdm directory: {}", dir);
            // Write default files here
        }
        else {
            logger.info(".chatdm directory already exists: {}", dir);
        }
        properties.setDir(dir.toString());
        logger.info(".chatdm directory set to: {}", dir);
    }
}
