package io.cote.chatdm;

import io.cote.chatdm.config.ChatDMProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
//        Path dir = properties.getChatDMDirPath();
//        if (!Files.exists(dir)) {
//            Files.createDirectories(dir);
//            logger.info("Created .chatdm directory: {}", dir);
//            // Write default files here
//        }
//        else {
//            logger.info(".chatdm directory already exists: {}", dir);
//        }
//
//        String[] directories = {"oracle", "journal", "prompts"}; // Replace with your directories in classpath
//        for (String dirName : directories) {
//            copyFilesFromClasspath(dir.resolve(dirName), "classpath:" + dirName + "/**");
//        }
//
//        logger.debug(".chatdm directory now populated {}", dir);
    }

//    private void copyFilesFromClasspath(Path targetDir, String classpathPattern) throws IOException, IOException {
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//
//        // Check if the target directory exists
//        if (Files.notExists(targetDir)) {
//            Files.createDirectories(targetDir);  // Create the target directory if it doesn't exist
//            logger.info("Created directory in .chatdm: {}", targetDir);
//        }
//
//        // Find all resources that match the pattern
//        Resource[] resources = resolver.getResources(classpathPattern);
//        for (Resource resource : resources) {
//            Path resourcePath = Path.of(resource.getURI());
//            Path relativePath = targetDir.resolve(resourcePath.getFileName());  // Get relative path in the target directory
//
//            // If it's a directory, create it in the target folder
//            if (Files.isDirectory(resourcePath)) {
//                Files.createDirectories(relativePath);
//                logger.info("Created directory: {}", relativePath);
//            } else {
//                // If it's a file, copy it if it doesn't already exist
//                if (Files.notExists(relativePath)) {
//                    Files.copy(resourcePath, relativePath, StandardCopyOption.REPLACE_EXISTING);
//                    logger.info("Copied file: {} to .chatdm", relativePath);
//                }
//            }
//        }
//    }
}
