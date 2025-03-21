package io.cote.chatdm.dmstore;

import io.cote.chatdm.config.ChatDMProperties;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FileDMStoreRepository implements DMStoreRepository {

    private final Path rootPath;

    // Inject ChatDMProperties into the constructor
    public FileDMStoreRepository(ChatDMProperties properties) {
        this.rootPath = properties.getDirPath().toAbsolutePath();
    }


    @Override
    public InputStream loadResource(String name) throws IOException {
        return Files.newInputStream(rootPath.resolve(name));
    }

    @Override
    public boolean exists(String name) {
        return Files.exists(rootPath.resolve(name));
    }

    @Override
    public void saveResource(String name, InputStream data) throws IOException {
        Path target = rootPath.resolve(name);
        Files.createDirectories(target.getParent());
        Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public List<String> listResources() {
        try (var files = Files.list(rootPath)) {
            return files.map(p -> p.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }


}