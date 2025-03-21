package io.cote.chatdm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix = "chatdm")
public class ChatDMProperties {

    @Value("${chatdm.dir:${user.home}/.chatdm}")
    private String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Path getDirPath() {
        return Path.of(dir);
    }
}