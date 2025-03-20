package io.cote.chatdm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Setups ip initial home directory or other stores for the ChatDM.
 */
@Component
public class ChatDMInit implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ChatDMInit.class);
    private final ChatDMProperties properties;

    public ChatDMInit(ChatDMProperties properties) {
        this.properties = properties;
    }

    @Value("${chatdm.dir:#{systemProperties['user.home'] + '/.chatdm'}}")
    private String chatDMDirProperty;
    private Path chatDMDir;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path dir = Path.of(chatDMDirProperty);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
            logger.debug("Created .chatdm directory: {}", dir);
            // Write default files here
        }
        properties.setDir(dir.toString());
        this.chatDMDir = dir;
        logger.debug(".chatdm directory set to: {}", dir);
    }
}
