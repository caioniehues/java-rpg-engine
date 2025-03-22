package io.cote.chatdm.oracle.repository;

import io.cote.chatdm.dmstore.FileDMStoreRepository;
import io.cote.chatdm.oracle.Oracle;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class FileOracleRepository implements OracleRepository {
    private static final Logger logger = LoggerFactory.getLogger(FileOracleRepository.class);
    private final OracleParser parser = new OracleParser();

    private final Map<String, Oracle> oracles = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final FileDMStoreRepository dmStore;

    public FileOracleRepository(FileDMStoreRepository dmStore) {
        this.dmStore = dmStore;
    }

    /**
     * Loads all oracles found in the classpath. Intended to be used at startup to populate the oracles. This will
     * replace any existing oracles in this repository that have already been loaded.
     */
    @PostConstruct
    void loadAllOracles() {
        Path oracleDir = dmStore.getDirPath().resolve("oracle/");

        if (!Files.exists(oracleDir)) {
            logger.error("Oracle directory does not exist: {}", oracleDir);
            return;
        }

        try (Stream<Path> paths = Files.walk(oracleDir)) {
            paths.filter(Files::isRegularFile) // Ensure we're working with files
                    .filter(path -> path.getFileName().toString().matches("chatdm\\.oracle\\..*\\.json")) // Match the regex pattern
                    .forEach(this::loadOracle);  // Process each matching file
        } catch (IOException e) {
            logger.error("Error reading oracle files from directory '{}'", oracleDir, e);
        }
    }

    private void loadOracle(Path filePath) {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            Oracle oracle = parser.parseOracleFile(filePath.getFileName().toString(), inputStream);
            oracles.put(oracle.name(), oracle);
            logger.debug("Loaded oracle '{}'", oracle.name());
        } catch (IOException e) {
            logger.error("Error reading oracle file '{}'", filePath.getFileName(), e);
        }
    }

    @Override
    public Set<String> findAllNames() {
        return Set.copyOf(oracles.keySet());
    }

    @Override
    public Oracle findByName(String name) {
        return oracles.get(name);
    }

    @Override
    public boolean existsByName(String name) {
        return oracles.containsKey(name);
    }

    @Override
    public List<Oracle> findAll() {
        // TK should this be List.of? Seemed like an estoric reason to use it.
        return List.copyOf(oracles.values());
    }
}
