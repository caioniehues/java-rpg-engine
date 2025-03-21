package io.cote.chatdm.oracle.repository;

import io.cote.chatdm.oracle.Oracle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

class OracleParser {
    private static final Logger logger = LoggerFactory.getLogger(OracleParser.class);
    private final Yaml yaml = new Yaml();

    Oracle parseOracleFile(String filename, InputStream inputStream) {
        Map<String, Object> yamlContent = yaml.load(inputStream);

        String name;
        LinkedHashMap<String, String> metadata = new LinkedHashMap<>();

        if (yamlContent.containsKey("metadata")) {
            Map<String, Object> yamlMetadata = (Map<String, Object>) yamlContent.get("metadata");
            yamlMetadata.forEach((key, value) -> metadata.put(key, value.toString()));
            name = metadata.getOrDefault("name", extractNameFromFilename(filename));
        } else {
            name = extractNameFromFilename(filename);
        }

        List<String> entries = new ArrayList<>();
        if (yamlContent.containsKey("entries")) {
            for (Object entry : (List<Object>) yamlContent.get("entries")) {
                entries.add(entry.toString());
            }
        }

        logger.info("Loaded oracle '{}' with {} entries", name, entries.size());
        return new Oracle(name, metadata, entries);
    }

    private String extractNameFromFilename(String filename) {
        return filename.replaceAll("^chatdm\\.oracle\\.|\\.yml$", "");
    }
}