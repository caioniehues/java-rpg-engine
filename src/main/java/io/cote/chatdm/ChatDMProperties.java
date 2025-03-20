package io.cote.chatdm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix = "chatdm")
public class ChatDMProperties {
    private String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Path getDirPath() {
        return Path.of(dir != null ? dir : System.getProperty("user.home") + "/.chatdm");
    }
}
