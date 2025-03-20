package io.cote.chatdm.oracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Class responsible for loading oracle YAML files.
 */
public class OracleReader {
    private static final Logger logger = LoggerFactory.getLogger(OracleReader.class);
    // TK need to make sure all of this file name stuff is case-insensitive
    private static final String ORACLE_DIR = "oracle";
    private static final String FILE_PREFIX = "chatdm.oracle.";
    // TK need to allow for .yaml files as well.
    private static final String FILE_EXTENSION = ".yml";

    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private final Yaml yaml = new Yaml();

    /**
     * Loads all oracle files from the classpath resources directory.
     *
     * @return A map of oracle names to Oracle objects
     */
    public Map<String, Oracle> loadFromClasspath() {
        Map<String, Oracle> oracles = new HashMap<>();

        try {
            // Look for files with pattern chatdm.oracle.*.yml in the classpath
            Resource[] resources = resourceResolver.getResources("classpath:" + ORACLE_DIR +"/" + FILE_PREFIX + "*" + FILE_EXTENSION);

            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    Oracle oracle = parseOracleFile(resource.getFilename(), inputStream);
                    oracles.put(oracle.getName(), oracle);
                } catch (Exception e) {
                    logger.error("Failed to parse oracle file: {}", resource.getFilename(), e);
                }
            }
        } catch (IOException e) {
            logger.error("Error loading oracle files from classpath", e);
        }

        return oracles;
    }

    /**
     * Loads all oracle files from a specified directory.
     *
     * @param directoryPath The path to the directory containing oracle files
     * @return A map of oracle names to Oracle objects
     */
    public Map<String, Oracle> loadFromDirectory(String directoryPath) {
        Map<String, Oracle> oracles = new HashMap<>();
        Path directory = Paths.get(directoryPath);

        if (!Files.isDirectory(directory)) {
            logger.error("Path is not a directory: {}", directoryPath);
            return oracles;
        }

        try (Stream<Path> paths = Files.walk(directory, 1)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> {
                        String filename = path.getFileName().toString();
                        return filename.startsWith(FILE_PREFIX) && filename.endsWith(FILE_EXTENSION);
                    })
                    .forEach(path -> {
                        try (InputStream inputStream = Files.newInputStream(path)) {
                            Oracle oracle = parseOracleFile(path.getFileName().toString(), inputStream);
                            oracles.put(oracle.getName(), oracle);
                        } catch (Exception e) {
                            logger.error("Failed to parse oracle file: {}", path.getFileName(), e);
                        }
                    });
        } catch (IOException e) {
            logger.error("Error loading oracle files from directory: {}", directoryPath, e);
        }

        return oracles;
    }

    /**
     * Parses a YAML file into an Oracle object.
     *
     * @param filename The name of the file
     * @param inputStream The input stream for the file
     * @return An Oracle object
     */
    @SuppressWarnings("unchecked")
    private Oracle parseOracleFile(String filename, InputStream inputStream) {
        Map<String, Object> yamlContent = yaml.load(inputStream);

        String name;
        LinkedHashMap<String, String> metadata = new LinkedHashMap<>();

        if (yamlContent.containsKey("metadata")) {
            Map<String, Object> yamlMetadata = (Map<String, Object>)yamlContent.get("metadata");

            // Convert all metadata values to strings and store in the metadata map
            for (Map.Entry<String, Object> entry : yamlMetadata.entrySet()) {
                metadata.put(entry.getKey(), entry.getValue().toString());
            }

            // Get name from metadata or use filename
            name = metadata.getOrDefault("name", extractNameFromFilename(filename));
        } else {
            name = extractNameFromFilename(filename);
        }

        // Extract entries
        List<String> entries = new ArrayList<>();
        if (yamlContent.containsKey("entries")) {
            for (Object entry : (List<Object>)yamlContent.get("entries")) {
                entries.add(entry.toString());
            }
        }

        logger.info("Loaded oracle '{}' with {} entries", name, entries.size());
        return new Oracle(name, metadata, entries);
    }

    private String extractNameFromFilename(String filename) {
        // Extract name from filename pattern chatdm.oracle.NAME.yml
        return filename.substring(FILE_PREFIX.length(), filename.length() - FILE_EXTENSION.length());
    }
}