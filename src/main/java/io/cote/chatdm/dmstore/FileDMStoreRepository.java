package io.cote.chatdm.dmstore;

import io.cote.chatdm.config.ChatDMProperties;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FileDMStoreRepository {

    private final Path rootPath;

    // Inject ChatDMProperties into the constructor
    public FileDMStoreRepository(ChatDMProperties properties) {
        this.rootPath = properties.getChatDMDirPath().toAbsolutePath();
    }

    public File load(String fileName) throws IOException {
        return rootPath.resolve(fileName).toFile();
    }

    public boolean exists(String name) {
        return Files.exists(rootPath.resolve(name));
    }

    public void append(String fileName, String content) {

    }

    public void save(String name, InputStream data) throws IOException {
        Path target = rootPath.resolve(name);
        Files.createDirectories(target.getParent());
        Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
    }


    public List<String> listResources() {
        try (var files = Files.list(rootPath)) {
            return files.map(p -> p.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }


}