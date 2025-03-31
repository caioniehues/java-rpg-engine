package io.cote.chatdm.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ChatDMProperties {

    @Value("${chatdm.dir:${user.home}/.chatdm}")
    private String dirPropertyValue;

    private Path chatDMRootDir;

    @Value("${chatdm.dirs:oracle,prompts,journal,dc}")
    private List<String> dirs;

    private List<Path> dirsPaths;

    @PostConstruct
    private void init() {
        chatDMRootDir = Path.of(this.dirPropertyValue);
        List<Path> dirsScratch = new ArrayList<Path>();
        for (String dir : this.dirs) {
            dirsScratch.add(chatDMRootDir.resolve(dir));
        }

        dirsPaths = Collections.unmodifiableList(dirsScratch);
    }

    /**
     * Get root directory of the chatdm directory.
     *
     * @return the directory. May or may not exist.
     */
    public Path getChatDMDirPath() {
        return chatDMRootDir;
    }

    /**
     * Get list of the sub-directories in the chatdm dir, e.g., oracle, journal, etc.
     *
     * @return the directories as {@link Path} objects. These may or may not exist.
     */
    public List<Path> getDirs() { return dirsPaths;}

}